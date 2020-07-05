package com.techflow.materialcolor.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.techflow.materialcolor.R
import com.techflow.materialcolor.databinding.ActivityAppIntroBinding
import com.techflow.materialcolor.utils.ThemeUtils

/**
 * Created by Dilip Suthar on 10/06/2020
 */
class AppIntroActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    // Static
    companion object {
        private lateinit var wizard_titles: Array<String>
        private lateinit var wizard_descs: Array<String>
    }

    private lateinit var bind: ActivityAppIntroBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_app_intro)

        initComponents()
    }

    private fun initComponents() {
        wizard_titles = resources.getStringArray(R.array.wizard_titles)
        wizard_descs = resources.getStringArray(R.array.wizard_descriptions)

        /*bottomProgressDots(0)*/
        bind.pageIndicator.count = wizard_titles.size
        bind.pageIndicator.selection = 0
        if (ThemeUtils.getTheme(this) == ThemeUtils.LIGHT) {
            bind.pageIndicator.unselectedColor = ContextCompat.getColor(this, R.color.overlay_dark_30)
        } else {
            bind.pageIndicator.unselectedColor = ContextCompat.getColor(this, R.color.overlay_light_30)
        }

        viewPagerAdapter = ViewPagerAdapter(this, bind.viewPager)
        bind.viewPager.adapter = viewPagerAdapter
        bind.viewPager.setOnPageChangeListener(this)
    }

    /*private fun bottomProgressDots(currentIndex: Int) {
        val dots = arrayOfNulls<ImageView>(MAX_STEP)

        bind.layoutDots.removeAllViews()
        for ((i, value) in dots.withIndex()) {
            dots[i] = ImageView(this)
            val width_height = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.shape_round)
            dots[i]?.setColorFilter(ContextCompat.getColor(this, R.color.grey_200), PorterDuff.Mode.SRC_IN)
            bind.layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[currentIndex]?.setImageResource(R.drawable.shape_round)
            dots[currentIndex]?.setColorFilter(ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimary), PorterDuff.Mode.SRC_IN)
        }
    }*/

    override fun onPageSelected(position: Int) {
        /*bottomProgressDots(position)*/
        bind.pageIndicator.selection = position
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }
    override fun onPageScrollStateChanged(state: Int) {

    }

    /**
     * View pager adapter
     */
    class ViewPagerAdapter(val activity: Activity, private val viewPager: ViewPager) : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater.inflate(R.layout.item_app_intro_wizard, container, false)
            view.findViewById<TextView>(R.id.title).text = wizard_titles[position]
            view.findViewById<TextView>(R.id.description).text = wizard_descs[position]

            val btnNext = view.findViewById<MaterialButton>(R.id.btn_next)
            if (position == wizard_titles.size - 1) {
                btnNext.text = "Get started"
            } else {
                btnNext.text = "Next"
            }

            btnNext.setOnClickListener {
                val current = viewPager.currentItem + 1
                if (current < wizard_titles.size) {
                    viewPager.currentItem = current
                } else {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.finish()
                }
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return wizard_titles.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}