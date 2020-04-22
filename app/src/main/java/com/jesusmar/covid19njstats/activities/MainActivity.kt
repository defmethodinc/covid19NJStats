package com.jesusmar.covid19njstats.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.util.Covid19Authentication
import com.jesusmar.covid19njstats.models.DailyData
import com.jesusmar.covid19njstats.models.ResponseData
import kotlinx.android.synthetic.main.activity_main.*
import com.jesusmar.covid19njstats.notification.Covid19FireBase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        last_update.text = ""

        val covidAuth =
            Covid19Authentication(this)

        covidAuth.setCallBack(object: Covid19Authentication.Covid19AuthCallBacks{
            override fun onSuccess(){

                getDailyData()
                getAllData()

                Covid19FireBase.setChannel(this@MainActivity)

                btn_state.setOnClickListener {
                    val stateIntent = Intent(this@MainActivity, StateHistory::class.java )
                    startActivity(stateIntent)
                }

                btn_essex.setOnClickListener {
                    val stateIntent = Intent(this@MainActivity, EssexHistory::class.java )
                    startActivity(stateIntent)
                }
            }

            override fun onFail() {
                Toast.makeText(this@MainActivity, "Authentication Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        } )
        covidAuth.runAuthentication()
    }

    private fun getDailyData() {
        val dataTask = GetDataFromAPITask(
            getString(R.string.todayUri)
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
                last_update.text = getString(
                    R.string.last_update,
                    if (response.dailyData.isNotEmpty()) sdf.format(response.dailyData[0].date) else "None")

                for (resp in response.dailyData){
                    when (resp.owner){
                        "NJ" -> {
                            tv_positive_state.text = getString(R.string.positive,resp.positive)
                            tv_negative_state.text = getString(R.string.negative,resp.negative)
                            tv_deaths_state.text = getString(R.string.deaths,resp.deaths)
                        }
                        "Essex" -> {
                            tv_positive_essex.text = getString(R.string.positive,resp.positive)
                            tv_negative_essex.text = getString(R.string.negative,resp.negative)
                            tv_deaths_essex.text = getString(R.string.deaths,resp.deaths)
                        }
                    }
                }
            }

            override fun onError() {
                Toast.makeText(this@MainActivity, "DEU SHABIU", Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }


    private fun getAllData() {
        val dataTask = GetDataFromAPITask(
            getString(R.string.allUri)
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val nJData = response.dailyData.filter { dataRow -> dataRow.owner == "NJ" }
                val essexData = response.dailyData.filter { dataRow -> dataRow.owner == "Essex" }

                val lineData = LineData(
                    getLineDataSet(nJData,"NJ", Color.rgb(104, 241, 175)),
                    getLineDataSet(essexData,"Essex",
                        R.color.colorPrimary,
                        R.color.colorPrimary
                    ))

                chart_all.data = lineData
                chart_all.invalidate()
            }

            override fun onError() {
                Toast.makeText(this@MainActivity, "Oops, please try again later ", Toast.LENGTH_SHORT).show()
            }
        })
        dataTask.getData()
    }

    private fun getLineDataSet(dailyData: List<DailyData>, name: String, color: Int, circle: Int = 0 ): LineDataSet {
        val entries = ArrayList<Entry>()
        for ((day, dataRow) in dailyData.withIndex()) {
            entries.add(BarEntry((day).toFloat(), dataRow.positive.toFloat()))
        }
        val dataSet = LineDataSet(entries, name)
        dataSet.color = color
        dataSet.lineWidth = 1.75f
        if (circle != 0){
            dataSet.circleRadius = 2f
            dataSet.circleHoleRadius = 1.0f
            dataSet.setCircleColor(circle)
        }

        return  dataSet
    }
}
