package com.jesusmar.covid19njstats.activities

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.components.CovidToolbar
import com.jesusmar.covid19njstats.models.GrowthData
import com.jesusmar.covid19njstats.models.ResponseDataGrowth
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import kotlinx.android.synthetic.main.activity_growth.*

class GrowthActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var llChartView: LinearLayout
    private lateinit var textTitle: TextView
    private lateinit var barChart: BarChart
    private lateinit var owner: String
    private lateinit var toolbar: CovidToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growth)

        owner = intent.extras?.get("owner") as String

        addToolbar()
        addScrollView()
        addChartView()
        attachViewsToParent()
        configConstraintLayout()

        getStateGrowth()

    }

    private fun configConstraintLayout() {
        val set = ConstraintSet()
        set.clone(mainLayout)

        set.connect(toolbar.id, ConstraintSet.TOP, mainLayout.id, ConstraintSet.TOP)
        set.connect(toolbar.id, ConstraintSet.END, mainLayout.id, ConstraintSet.END)
        set.connect(toolbar.id, ConstraintSet.START, mainLayout.id, ConstraintSet.START)

        set.connect(scrollView.id, ConstraintSet.BOTTOM, mainLayout.id, ConstraintSet.BOTTOM)
        set.connect(scrollView.id, ConstraintSet.END, mainLayout.id, ConstraintSet.END)
        set.connect(scrollView.id, ConstraintSet.START, mainLayout.id, ConstraintSet.START)
        set.connect(scrollView.id, ConstraintSet.TOP, toolbar.id, ConstraintSet.BOTTOM)
        set.applyTo(mainLayout)
    }

    private fun attachViewsToParent() {
        llChartView.addView(textTitle)
        llChartView.addView(barChart)
        scrollView.addView(llChartView)
        mainLayout.addView(toolbar, 0)
        mainLayout.addView(scrollView, 1)
    }

    private fun addToolbar() {
        toolbar = CovidToolbar(this, "$owner History")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun addChartView() {
        llChartView = LinearLayout(this)
        llChartView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llChartView.orientation = LinearLayout.VERTICAL
        textTitle = TextView(this)
        val linearLayout = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.topMargin = 10
        with(textTitle){
            layoutParams = linearLayout
            setPadding(45, 0, 15, 0)
            textSize = 20F
            setTypeface(null, Typeface.BOLD)
            text = getString(R.string.daily_growth, owner)
        }

        barChart = BarChart(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT )
        params.setMargins(10,10,10,10)
        barChart.layoutParams = params

    }

    private fun addScrollView() {
        scrollView = ScrollView(this)
        with(scrollView) {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            id = View.generateViewId()
            isFillViewport = true
        }
    }


    private fun getStateGrowth() {

        val dataTask = GetDataFromAPITask(
            this,
            getString(R.string.api_growth),
            owner
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: Any?) {
                val stateDataGrowth: List<GrowthData> = (data as ResponseDataGrowth).dailyData
                val entries = ArrayList<BarEntry>()
                for ((day, dataRow) in stateDataGrowth.withIndex()) {
                    entries.add(BarEntry(day.toFloat(), dataRow.value.toFloat()))
                }

                val dataSet = BarDataSet(entries, getString(R.string.percent_per_day))

                dataSet.color = R.color.colorPrimaryDark

                val formatter = object: ValueFormatter(){
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return "${value.toInt()}%"
                    }
                }


                val lineData = BarData(dataSet)


                lineData.setValueTextSize(8f)

                lineData.setDrawValues(false)
                barChart.data = lineData
                with(barChart) {
                    axisRight.setValueFormatter(formatter)
                    xAxis.setLabelCount(entries.size / 5, true)
                    xAxis.axisMinimum = 0F
                    axisLeft.axisMinimum = 0F
                    axisRight.axisMinimum = 0F
                    axisRight.setLabelCount(10, true)
                    axisLeft.setLabelCount(0, true)
                    description.setPosition(760F, 100F)
                    axisLeft.setDrawLabels(false)
                    description.text = "Growth percentage per day"
                    description.textSize = 14F
                    invalidate()
                }
            }

            override fun onError() {
                Toast.makeText(this@GrowthActivity, getString(R.string.error_growth_state)
                    , Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }
}
