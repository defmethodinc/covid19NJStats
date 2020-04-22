package com.jesusmar.covid19njstats.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.DailyData
import com.jesusmar.covid19njstats.models.ResponseData
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

        getEssexData()
    }

    private fun getEssexData() {

        val dataTask = GetDataFromAPITask(
            getString(R.string.urlEssex)
        )

        val dataListener = object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val mEssexData: List<DailyData> = response.dailyData
                val entries = ArrayList<BarEntry>()
                for ((day, dailyDataRow) in mEssexData.withIndex()) {
                    entries.add(BarEntry((day).toFloat(), dailyDataRow.positive.toFloat()))
                }
                val dataSet = BarDataSet(entries, "Accumulative")
                val lineData = BarData(dataSet)
                chart_essex.data = lineData
                chart_essex.invalidate()
            }
            override fun onError() {
                Toast.makeText(this@EssexHistory, "Ops, could not get data from the server",
                    Toast.LENGTH_SHORT).show()
            }
        }
        dataTask.setDataListener(dataListener)
        dataTask.getData()
    }

}
