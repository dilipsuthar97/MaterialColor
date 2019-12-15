package com.techflow.materialcolor.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.techflow.materialcolor.R
import com.techflow.materialcolor.adapter.AdapterColor
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.database.AppDatabase
import com.techflow.materialcolor.databinding.ActivityColorBinding
import com.techflow.materialcolor.helpers.AppExecutorHelper
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.Tools
import kotlinx.android.synthetic.main.activity_color.*

/**
 * @author Dilip Suthar
 */
class ColorActivity : BaseActivity(), AdapterColor.OnItemClickListener {

    private val TAG = "MaterialColor"

    private lateinit var bind: ActivityColorBinding
    private lateinit var listColor: ArrayList<Color>

    private var mDb: AppDatabase? = null
    private lateinit var adapterColor: AdapterColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_color)
        mDb = AppDatabase.getInstance(this)

        val extras = intent.extras
        val colorName = extras!!.getString("COLOR_NAME")!!
        val color = extras.getInt("COLOR")
        Log.d(TAG, "colorname: $colorName")
        Log.d(TAG, "color: $color")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }

        initToolbar(colorName, color)
        initComponent(colorName)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    /**
     * @func init toolbar config
     * @param colorName name of the color
     * @param color int value of color
     */
    private fun initToolbar(colorName: String, color: Int) {
        setSupportActionBar(bind.toolbar as Toolbar)
        bind.toolbar.setBackgroundColor(color)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = colorName
        Tools.setSystemBarColorById(this, color)
    }

    /**
     * @func init all component's config
     * @param colorName name of the color
     */
    private fun initComponent(colorName: String) {

        listColor = when (colorName) {
            "Red" -> DataGenerator.getRedColorData(this)
            "Pink" -> DataGenerator.getPinkColorData(this)
            "Purple" -> DataGenerator.getPurpleColorData(this)
            "Deep Purple" -> DataGenerator.getDeepPurpleColorData(this)
            "Indigo" -> DataGenerator.getIndigoColorData(this)
            "Blue" -> DataGenerator.getBlueColorData(this)
            "Light Blue" -> DataGenerator.getLightBlueColorData(this)
            "Cyan" -> DataGenerator.getCyanColorData(this)
            "Teal" -> DataGenerator.getTealColorData(this)
            "Green" -> DataGenerator.getGreenColorData(this)
            "Light Green" -> DataGenerator.getLightGreenColorData(this)
            "Lime" -> DataGenerator.getLimeColorData(this)
            "Yellow" -> DataGenerator.getYellowColorData(this)
            "Amber" -> DataGenerator.getAmberColorData(this)
            "Orange" -> DataGenerator.getOrangeColorData(this)
            "Deep Orange" -> DataGenerator.getDeepOrangeColorData(this)
            "Brown" -> DataGenerator.getBrownColorData(this)
            "Grey" -> DataGenerator.getGreyColorData(this)
            else -> DataGenerator.getBlueGreyColorData(this)
        }

        // Add AdView for each 5 steps >>>>>>>>>>
        /*var adCount = 0
        for (i in listColor.indices) {
            if (i % 5 == 0) {
                listColor.add(adCount, Color(Color.TYPE_AD, -1, "", ""))
                adCount += 5 + 1
            }
        }*/

        adapterColor = AdapterColor(this, this, this, mDb)

        // Recycler view
        with(bind.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ColorActivity)
            adapter = adapterColor
        }

        adapterColor.setColors(listColor)
    }

    /**
     * @inherited on item click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemClick(view: View, color: Color, position: Int) {
        Tools.copyToClipboard(this@ColorActivity, color.colorCode, "HEX code ${color.colorCode}")
    }

    /**
     * @inherited on item long click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemLongClick(view: View, color: Color, position: Int) {
    }

    /**
     * @inherited on bookmark button click ballback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onBookmarkButtonClick(view: View, color: Color, position: Int) {
        AnimUtils.bounceAnim(view)

        if (adapterColor.getBookmarkedValue(position)) {
            AppExecutorHelper.getInstance()?.diskIO()?.execute {
                mDb?.colorDao()?.removeColor(adapterColor.getColor(position).colorCode)

                AppExecutorHelper.getInstance()?.mainThread()?.execute {
                    this.displayToast("${color.colorCode} removed from bookmark")
                    val btn = view as ImageButton
                    btn.setImageResource(R.drawable.ic_bookmark_border)
                    adapterColor.setBookmarkedValue(position, false)
                }
            }

        }
        else {
            AppExecutorHelper.getInstance()?.diskIO()?.execute {
                mDb?.colorDao()?.saveColor(adapterColor.getColor(position))

               AppExecutorHelper.getInstance()?.mainThread()?.execute {
                   this.displayToast("${color.colorCode} bookmarked")
                   val btn = view as ImageButton
                   btn.setImageResource(R.drawable.ic_bookmark)
                   adapterColor.setBookmarkedValue(position, true)
               }
            }

        }
    }
}
