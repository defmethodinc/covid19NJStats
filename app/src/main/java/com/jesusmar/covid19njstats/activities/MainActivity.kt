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
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.jesusmar.covid19njstats.R
import com.jesusmar.covid19njstats.models.DailyData
import com.jesusmar.covid19njstats.models.ResponseData
import com.jesusmar.covid19njstats.notification.Covid19FireBase
import com.jesusmar.covid19njstats.util.Auth0AuthenticationTask
import com.jesusmar.covid19njstats.util.Covid19Authentication
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_card_essex_content.*
import kotlinx.android.synthetic.main.fragment_card_state_content.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        last_update.text = ""

        tabs.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabLayoutAdapter(supportFragmentManager, tabs.tabCount, tabs.tabCount)

        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

        })

        val covid19Auth =
            Covid19Authentication(this)

        covid19Auth.setCallBack(object : Covid19Authentication.Covid19AuthCallBacks {
            override fun onSuccess() {
                Covid19FireBase.setChannel(this@MainActivity)
                val auth0Authentication = Auth0AuthenticationTask(this@MainActivity)
                auth0Authentication.setListener(object: Auth0AuthenticationTask.Auth0Listener{
                    override fun success() {
                        getDailyData()
                        getAllData()
                        btn_essex.setOnClickListener {
                            val stateIntent = Intent(this@MainActivity, EssexHistory::class.java)
                                startActivity(stateIntent)
                            }

                        btn_state.setOnClickListener {
                                    val stateIntent = Intent(this@MainActivity, StateHistory::class.java)
                                    startActivity(stateIntent)
                        }
                    }

                    override fun fail() {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.api_authentication_failed_tryagan_later), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
                auth0Authentication.getToken()
            }

            override fun onFail() {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.biometric_authentication_failed_tryagan_later), Toast.LENGTH_SHORT
                ).show()
            }
        })
        covid19Auth.runAuthentication()
    }

    private fun getDailyData() {
        val dataTask = GetDataFromAPITask(
            getString(R.string.todayUri),
            this
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
                last_update.text = getString(
                    R.string.last_update,
                    if (response.dailyData.isNotEmpty()) sdf.format(response.dailyData[0].date) else "None"
                )

                for (resp in response.dailyData) {
                    when (resp.owner) {
                        getString(R.string.NJ) -> {
                            tv_positive_state_value.text = resp.positive.toString()
                            tv_negative_state_value.text = resp.negative.toString()
                            tv_deaths_state_value.text = resp.deaths.toString()
                        }
                        getString(R.string.essex) -> {
                            tv_positive_essex_value.text = resp.positive.toString()
                            tv_negative_essex_value.text = resp.negative.toString()
                            tv_deaths_essex_value.text = resp.deaths.toString()
                        }
                    }
                }
            }

            override fun onError() {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.try_again_latter),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        dataTask.getData()
    }


    private fun getAllData() {
        val dataTask = GetDataFromAPITask(
            getString(R.string.allUri),
            this
        )

        dataTask.setDataListener(object :
            GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
                val response = Gson().fromJson(data, ResponseData::class.java)
                val nJData =
                    response.dailyData.filter { dataRow -> dataRow.owner == getString(R.string.NJ) }
                val essexData =
                    response.dailyData.filter { dataRow -> dataRow.owner == getString(R.string.essex) }
                setupChart(nJData, essexData)
            }

            override fun onError() {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.try_again_latter),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        dataTask.getData()
    }

    private fun getLineDataSet(
        dailyData: List<DailyData>,
        name: String,
        color: Int,
        circle: Int = 0
    ): LineDataSet {
        val entries = ArrayList<Entry>()
        for ((day, dataRow) in dailyData.withIndex()) {
            entries.add(BarEntry((day).toFloat(), dataRow.positive.toFloat()))
        }
        val dataSet = LineDataSet(entries, name)
        dataSet.color = color
        dataSet.lineWidth = 1.75f
        if (circle != 0) {
            dataSet.circleRadius = 2f
            dataSet.circleHoleRadius = 1.0f
            dataSet.setCircleColor(circle)
        }

        return dataSet
    }

    private fun setupChart(
        nJData: List<DailyData>,
        essexData: List<DailyData>
    ) {
        val lineData = LineData(
            getLineDataSet(nJData, getString(R.string.NJ), Color.rgb(104, 241, 175)),
            getLineDataSet(
                essexData, getString(R.string.essex),
                R.color.colorPrimary,
                R.color.colorPrimary
            )
        )

        lineData.setValueTextSize(0f)
        chart_all.xAxis.setLabelCount(nJData.size / 5, true)
        chart_all.xAxis.axisMinimum = 0F
        chart_all.axisLeft.axisMinimum = 0F
        chart_all.axisRight.axisMinimum = 0F
        chart_all.axisRight.setLabelCount(10, true)
        chart_all.axisLeft.setLabelCount(0, true)
        chart_all.description.setPosition(760F, 100F)
        chart_all.legend.textSize = 18F
        chart_all.axisLeft.setDrawLabels(false)
        chart_all.description.text= ""
        chart_all.description.textSize = 14F
        chart_all.data = lineData
        chart_all.axisLeft.setDrawGridLines(false)
        chart_all.axisRight.setDrawGridLines(false)
        chart_all.xAxis.setDrawGridLines(false)


        chart_all.invalidate()
    }

}
