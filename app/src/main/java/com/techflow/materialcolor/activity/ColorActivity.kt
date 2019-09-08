package com.techflow.materialcolor.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.techflow.materialcolor.R
import com.techflow.materialcolor.adapter.AdapterColor
import com.techflow.materialcolor.data.DataGenerator
import com.techflow.materialcolor.model.Color
import com.techflow.materialcolor.utils.Tools
import kotlinx.android.synthetic.main.activity_color.*

class ColorActivity : BaseActivity() {

    private val TAG = "MaterialColor"

    private lateinit var listColor: List<Color>
    private lateinit var mOnItemClickListener: AdapterColor.OnItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color)

        val extras = intent.extras
        val colorName = extras!!.getString("COLOR_NAME")!!
        val color = extras.getInt("COLOR")
        val pos = extras.getInt("COLOR_POS")
        Log.d(TAG, "colorname: $colorName")
        Log.d(TAG, "color: $color")
        Log.d(TAG, "pos: $pos")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }

        initToolbar(colorName, color)
        initComponent(pos)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }

    /** Methods */
    private fun initToolbar(colorName: String, color: Int) {
        setSupportActionBar(include_toolbar as Toolbar)
        include_toolbar.setBackgroundColor(color)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = colorName
        Tools.setSystemBarColorById(this, color)
    }

    private fun initComponent(pos: Int) {

        /** Listener */
        mOnItemClickListener = object : AdapterColor.OnItemClickListener {

            override fun onItemClick(view: View, color: Color, position: Int) {

            }

            override fun onItemLongClick(view: View, color: Color, position: Int) {
                Tools.copyToClipboard(this@ColorActivity, color.colorCode, "HEX code ${color.colorCode}")
            }
        }

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        listColor = when (pos) {
            0 -> DataGenerator.getRedColorData(this)
            1 -> DataGenerator.getPinkColorData(this)
            2 -> DataGenerator.getPurpleColorData(this)
            3 -> DataGenerator.getDeepPurpleColorData(this)
            4 -> DataGenerator.getIndigoColorData(this)
            5 -> DataGenerator.getBlueColorData(this)
            6 -> DataGenerator.getLightBlueColorData(this)
            7 -> DataGenerator.getCyanColorData(this)
            8 -> DataGenerator.getTealColorData(this)
            9 -> DataGenerator.getGreenColorData(this)
            10 -> DataGenerator.getLightGreenColorData(this)
            11 -> DataGenerator.getLimeColorData(this)
            12 -> DataGenerator.getYellowColorData(this)
            13 -> DataGenerator.getAmberColorData(this)
            14 -> DataGenerator.getOrangeColorData(this)
            15 -> DataGenerator.getDeepOrangeColorData(this)
            16 -> DataGenerator.getBrownColorData(this)
            17 -> DataGenerator.getGreyColorData(this)
            else -> DataGenerator.getBlueGreyColorData(this)
        }

        val adapter = AdapterColor(ArrayList(listColor), this, this, mOnItemClickListener)
        recycler_view.adapter = adapter
    }
}
