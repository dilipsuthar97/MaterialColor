package com.techflow.materialcolor.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.techflow.materialcolor.MaterialColor
import com.techflow.materialcolor.R
import com.techflow.materialcolor.adapter.AdapterColor
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.db.AppDatabase
import com.techflow.materialcolor.databinding.ActivitySocialColorsBinding
import com.techflow.materialcolor.helpers.AppExecutorHelper
import com.techflow.materialcolor.helpers.displayToast
import com.techflow.materialcolor.helpers.isTablet
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.AnimUtils
import com.techflow.materialcolor.utils.ColorUtils
import com.techflow.materialcolor.utils.ThemeUtils
import com.techflow.materialcolor.utils.Tools

/**
 * Created by Dilip Suthar on 29/05/2020
 */
class SocialColorsActivity : BaseActivity(), AdapterColor.OnItemClickListener {
    private val TAG = SocialColorsActivity::class.java.simpleName

    private lateinit var bind: ActivitySocialColorsBinding
    private lateinit var listColor: ArrayList<Color>

    private var mDb: AppDatabase? = null
    private lateinit var adapterColor: AdapterColor
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_social_colors)
        mDb = AppDatabase.getInstance(this)

        initToolbar()
        initComponent()
    }

    /**
     * @func init toolbar config
     */
    private fun initToolbar() {
        setSupportActionBar(bind.toolbar as MaterialToolbar)
        val actionBar = supportActionBar!!
        actionBar.title = "Social Colors"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        (bind.toolbar as MaterialToolbar).setNavigationIcon(R.drawable.ic_arrow_back)
        Tools.changeNavigationIconColor(bind.toolbar as MaterialToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorTextPrimary))
    }

    /**
     * @func init all component's config
     */
    private fun initComponent() {
        listColor = DataGenerator.getSocialColorData(this)

        adapterColor = AdapterColor(this, this, this, mDb)

        // Recycler view
        with(bind.recyclerView) {
            setHasFixedSize(true)
            layoutManager =
                if (context?.isTablet()!!) GridLayoutManager(applicationContext, 2) else LinearLayoutManager(applicationContext)
            adapter = adapterColor
        }

        adapterColor.setColors(listColor)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    /**
     * @inherited on item click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemClick(view: View, color: Color, position: Int) {
        HomeActivity.firebaseAnalytics.logEvent(MaterialColor.FIREBASE_EVENT_COPY_HEX_CODE, null)
        Tools.copyToClipboard(this, color.colorCode, "HEX code ${color.colorCode}")
    }

    /**
     * @inherited on item long click callback
     * @param view view
     * @param color color object
     * @param position recycler view item position
     */
    override fun onItemLongClick(view: View, color: Color, position: Int) {
        ColorUtils.executeColorCodePopupMenu(this, color.colorCode, view)
    }

    /**
     * @inherited on bookmark button click callback
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
                    toast?.cancel()
                    toast = this.displayToast("${color.colorCode} removed from bookmark")
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
                    toast?.cancel()
                    toast = this.displayToast("${color.colorCode} bookmarked")
                    val btn = view as ImageButton
                    btn.setImageResource(R.drawable.ic_bookmark)
                    adapterColor.setBookmarkedValue(position, true)
                }
            }

        }
    }
}