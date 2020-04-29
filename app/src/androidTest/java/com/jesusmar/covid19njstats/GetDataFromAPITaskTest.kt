package com.jesusmar.covid19njstats

import android.content.Context
import android.test.mock.MockContext
import androidx.test.platform.app.InstrumentationRegistry
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
        context = InstrumentationRegistry.getInstrumentation().targetContext
        signal = CountDownLatch(1)
    }

    @After
    fun tearDown() {
        signal!!.countDown()
    }

    @Test
    fun teste()
    {

        var response: String? = null
        task =
            GetDataFromAPITask("https://covid19njapi.herokuapp.com/today", context )

        task.setDataListener(object : GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
             response = data
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
