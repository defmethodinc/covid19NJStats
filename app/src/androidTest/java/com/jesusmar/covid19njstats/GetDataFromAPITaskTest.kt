package com.jesusmar.covid19njstats

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.jesusmar.covid19njstats.models.ResponseData
import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch


class GetDataFromAPITaskTest {

    private var signal: CountDownLatch? = null
    lateinit var task : GetDataFromAPITask
    lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        signal = CountDownLatch(1)
    }

    @After
    fun tearDown() {
        signal!!.countDown()
    }

    @Test
    fun getDataFromAPI()
    {

        var response: ResponseData? = null
        task =
            GetDataFromAPITask("/today", context )

        task.setDataListener(object : GetDataFromAPITask.DataListener {
            override fun onSuccess(data: Any?) {
             response = (data as ResponseData)
             signal!!.countDown()
            }

            override fun onError() {
                TODO("Not yet implemented")
            }
        }).getData()
        signal!!.await()

        Assert.assertTrue(response.toString().contains("dailyData"))


    }

}
