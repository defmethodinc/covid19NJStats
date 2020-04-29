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
import com.jesusmar.covid19njstats.models.ResponseDataGrowth
import kotlinx.android.synthetic.main.activity_essex_history.*
import kotlinx.android.synthetic.main.activity_state_history.*

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
            getString(R.string.growth_essex), this
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseDataGrowth::class.java)
                val stateDataGrowth: List<GrowthData> = response.dailyData
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
                chart_essex.axisRight.setValueFormatter(formatter)
                chart_essex.xAxis.setLabelCount(entries.size / 5, true)
                chart_essex.xAxis.axisMinimum = 0F
                chart_essex.axisLeft.axisMinimum = 0F
                chart_essex.axisRight.axisMinimum = 0F
                chart_essex.axisRight.setLabelCount(10, true)
                chart_essex.axisLeft.setLabelCount(0, true)
                chart_essex.description.setPosition(760F, 100F)
                chart_essex.axisLeft.setDrawLabels(false)
                chart_essex.description.text = "Growth percentage per day"
                chart_essex.description.textSize = 14F



                chart_essex.data = lineData
                chart_essex.invalidate()



            }

            override fun onError() {
                Toast.makeText(this@EssexHistory, getString(R.string.error_growth_state)
                    , Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }


}
