package com.jesusmar.covid19njstats.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.GrowthData
import com.jesusmar.covid19njstats.models.ResponseData
import com.jesusmar.covid19njstats.models.ResponseDataGrowth
import kotlinx.android.synthetic.main.activity_state_history.*
import kotlin.collections.ArrayList


class StateHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_history)

        setSupportActionBar(tb_add_place)
        tb_add_place.title = getString(R.string.stateHistoryTitle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        getStateGrowth()
    }

    private fun getStateGrowth() {

            val dataTask = GetDataFromAPITask(
                getString(R.string.api_growth_nj), this
            )

            dataTask.setDataListener(object :
                GetDataFromAPITask.DataListener {
            override fun onSuccess(data: Any?) {
                val stateDataGrowth: List<GrowthData> = (data as ResponseDataGrowth).dailyData
                val entries = ArrayList<Entry>()
                for ((day, dataRow) in stateDataGrowth.withIndex()) {
                    entries.add(Entry(day.toFloat(), dataRow.value.toFloat()))
                }

                val dataSet = LineDataSet(entries, getString(R.string.percent_per_day))
                dataSet.highlightLineWidth = 1F


                dataSet.color = R.color.colorPrimaryDark
                dataSet.lineWidth = 2.75f
                dataSet.circleRadius = 1f
                dataSet.circleColors = listOf(R.color.colorPrimaryDark)
                dataSet.circleHoleRadius = 1.0f



                val formatter = object: ValueFormatter(){
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return "${value.toInt()}%"
                    }
                }

                val lineData = LineData(dataSet)
                lineData.setValueTextSize(8f)

                chart_growthState.data = lineData
                with(chart_growthState) {
                    axisRight.setValueFormatter(formatter)
                    xAxis.setLabelCount(entries.size / 5, true)
                    xAxis.axisMinimum = 0F
                    axisLeft.axisMinimum = 0F
                    axisRight.axisMinimum = 0F
                    axisRight.setLabelCount(10, true)
                    axisLeft.setDrawLabels(false)
                    description.text = ""
                    description.textSize = 14F
                    invalidate()
                }



            }

            override fun onError() {
                Toast.makeText(this@StateHistory, getString(R.string.error_growth_state)
                    , Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }
}
