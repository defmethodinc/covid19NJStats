package com.jesusmar.covid19njstats.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.*
import com.google.gson.Gson
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.DailyData
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

        getStateData()
        getStateGrowth()
    }

    private fun getStateData() {

        val dataTask = GetDataFromAPITask(
            getString(R.string.stateURL)
        )

        val listener = object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val stateData: List<DailyData>  = response.dailyData
                val entries = ArrayList<BarEntry>()
                for ((day, dataRow) in stateData.withIndex()) {
                    entries.add(BarEntry((day).toFloat(), dataRow.positive.toFloat()))
                }

                val dataSet = BarDataSet(entries, "Accumulative")
                val barData = BarData(dataSet)
                barData.setDrawValues(false)
                chart.data = barData
                chart.invalidate()
            }

            override fun onError() {
                Toast.makeText(this@StateHistory,
                    getString(R.string.error_state_data_message),
                    Toast.LENGTH_SHORT).show()
            }
        }

        dataTask.setDataListener(listener)
        dataTask.getData()
    }

    private fun getStateGrowth() {

            val dataTask = GetDataFromAPITask(
                getString(R.string.growthNJ)
            )

            dataTask.setDataListener(object :
                GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseDataGrowth::class.java)
                val stateDataGrowth: List<GrowthData> = response.dailyData
                val entries = ArrayList<Entry>()
                for ((day, dataRow) in stateDataGrowth.withIndex()) {
                    entries.add(Entry(day.toFloat(), dataRow.value.toFloat()))
                }

                val dataSet = LineDataSet(entries, getString(R.string.percent_per_day))
                val lineData = LineData(dataSet)
                chart_growthState.data = lineData
                chart_growthState.invalidate()
            }

            override fun onError() {
                Toast.makeText(this@StateHistory, getString(R.string.error_growth_state)
                    , Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }
}
