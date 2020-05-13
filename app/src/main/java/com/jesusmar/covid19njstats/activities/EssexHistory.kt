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
import kotlinx.android.synthetic.main.activity_essex_history.*

class EssexHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_essex_history)
        setSupportActionBar(tb_add_place_essex)
        tb_add_place_essex.title = "Essex History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb_add_place_essex.setNavigationOnClickListener {
            onBackPressed()
        }

        getStateGrowth()
    }

    private fun getStateGrowth() {

        val dataTask = GetDataFromAPITask(
            getString(R.string.api_growth_essex), this
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
                chart_essex.data = lineData
                with(chart_essex) {
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
                Toast.makeText(this@EssexHistory, getString(R.string.error_growth_state)
                    , Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }


}
