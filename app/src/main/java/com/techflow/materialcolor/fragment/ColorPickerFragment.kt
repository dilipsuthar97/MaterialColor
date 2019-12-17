package com.techflow.materialcolor.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.techflow.materialcolor.R

import com.techflow.materialcolor.databinding.FragmentColorPickerBinding
import com.techflow.materialcolor.model.ColorFromImage
import com.techflow.materialcolor.utils.AnimUtils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.afollestad.materialdialogs.MaterialDialog
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.techflow.materialcolor.activity.HomeActivity
import com.techflow.materialcolor.adapter.AdapterColorFromImage
import com.techflow.materialcolor.helpers.PermissionHelper
import com.techflow.materialcolor.utils.Preferences
import com.techflow.materialcolor.utils.SharedPref
import com.techflow.materialcolor.utils.Tools
import java.util.*

/**
 * Modified by DILIP SUTHAR on 29/08/19
 */
class ColorPickerFragment : Fragment() {

    companion object {
        fun getInstance(): ColorPickerFragment {
            return ColorPickerFragment()
        }
    }

    private lateinit var binding: FragmentColorPickerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_color_picker, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        showTutorial()

        binding.btnImgChooser.setOnClickListener {
            // Load ad
            if (SharedPref.getInstance(context!!).getBoolean(Preferences.SHOW_AD, true))
                HomeActivity.showInterstitialAd(context!!)

            AnimUtils.bounceAnim(it)
            when {
                PermissionHelper.isPermissionsGranted(context!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)) ->
                    openImagePicker()
                ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) ->
                    showPermissionDeniedDialog()
                else ->
                    PermissionHelper.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    }

    /**
     * @func show permission denied dialog
     */
    private fun showPermissionDeniedDialog() {
        MaterialDialog(context!!).show {
            cornerRadius(16f)
            title(text = "Permission denied previously")
            message(text = "Without the STORAGE permission the app is unable to load image. Are you sure you want to deny this permission?")
            positiveButton(text = "I'm sure") {}
            negativeButton(text = "Retry") {
                PermissionHelper.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    }

    /**
     * @func open image chooser activity
     */
    private fun openImagePicker() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .setAspectRatio(2, 1)
            .start(context!!, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == -1) {
                    binding.imgPhoto.setImageURI(result.uri)
                    extractColorFromImage()
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    result.error
            }
        }
    }

    /**
     * @func extract colors from choosen image
     */
    private fun extractColorFromImage() {
        val bitmap = if (binding.imgPhoto.drawable is BitmapDrawable)
            (binding.imgPhoto.drawable as BitmapDrawable).bitmap
        else {
            val drawable = binding.imgPhoto.drawable
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        Palette.from(bitmap).generate {
            val palettes = mutableListOf<ColorFromImage>()
            with(it!!) {
                for (i in 0..5) {
                    var color: Int
                    when (i) {
                        0 -> {
                            color = getVibrantColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                        1 -> {
                            color = getLightVibrantColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                        2 -> {
                            color = getDarkVibrantColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                        3 -> {
                            color = getMutedColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                        4 -> {
                            color = getLightMutedColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                        5 -> {
                            color = getDarkMutedColor(0)
                            if (color != 0) palettes.add(ColorFromImage(color, getHexCode(color)))
                        }
                    }
                }
            }

            Tools.inVisibleViews(binding.imgDummy, type = Tools.InvisibilityType.GONE)
            Tools.visibleViews(binding.lytColorPalette)

            val adapterColorFromImage = AdapterColorFromImage(palettes, context!!)
            with(binding.recyclerView) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapterColorFromImage
            }
        }
    }

    /**
     * @func show app intro for first use
     */
    private fun showTutorial() {
        with(SharedPref.getInstance(context!!)) {
            if (getBoolean(Preferences.ColorPickerFragFR, true)) {
                TapTargetView.showFor(activity!!,
                    TapTarget.forView(
                        binding.btnImgChooser,
                        "Tap here!",
                        "Tap here to choose image and extract colors from image.")
                        .outerCircleColor(R.color.colorAccent)
                        .outerCircleAlpha(0.95f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .cancelable(false)
                        .targetRadius(90)
                    /*object : TapTargetView.Listener() {
                        override fun onTargetClick(view: TapTargetView?) {
                            when {
                                checkPermission() -> openImagePicker()
                                ActivityCompat.shouldShowRequestPermissionRationale(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) -> showPermissionDeniedDialog()
                                else -> requestPermission()
                            }
                        }
                    }*/
                )

                saveData(Preferences.ColorPickerFragFR, false)
            }
        }
    }

    /**
     * @func convert color's int value into hex code
     * @param color int value of color
     *
     * @return return string value of hex code
     */
    private fun getHexCode(color: Int): String {
        return "#${Integer.toHexString(color).toUpperCase(Locale.getDefault()).substring(2)}"  // Without alpha
        //return "#${Integer.toHexString(color).toUpperCase()}"  // With alpha
    }
}
