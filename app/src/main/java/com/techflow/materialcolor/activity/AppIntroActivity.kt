package com.techflow.materialcolor.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        private const val MAX_STEP = 4
        private val title_array = arrayOf(
            "Ready to Travel",
            "Pick the Ticket",
            "Flight to Destination",
            "Enjoy Holiday"
        )
        private val description_array = arrayOf(
            "Choose your destination, plan Your trip. Pick the best place for Your holiday",
            "Select the day, pick Your ticket. We give you the best prices. We guarantee!",
            "Safe and Comfort flight is our priority. Professional crew and services.",
            "Enjoy your holiday, Don't forget to feel the moment and take a photo!"
        )
    }

    private lateinit var bind: ActivityAppIntroBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_app_intro)

        /*bottomProgressDots(0)*/
        bind.pageIndicator.count = MAX_STEP
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
            view.findViewById<TextView>(R.id.title).text = title_array[position]
            view.findViewById<TextView>(R.id.description).text = description_array[position]

            val btnNext = view.findViewById<MaterialButton>(R.id.btn_next)
            if (position == title_array.size - 1) {
                btnNext.text = "Get started"
            } else {
                btnNext.text = "Next"
            }

            btnNext.setOnClickListener {
                val current = viewPager.currentItem + 1
                if (current < MAX_STEP) {
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
            return MAX_STEP
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