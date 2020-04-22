package com.jesusmar.covid19njstats

import com.jesusmar.covid19njstats.util.GetDataFromAPITask
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch


class GetDataFromAPITaskTest {

    private var signal: CountDownLatch? = null
    lateinit var task : GetDataFromAPITask

    @Before
    fun setUp() {
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
            GetDataFromAPITask("","https://covid19njapi.herokuapp.com/today")

        task.setDataListener(object : GetDataFromAPITask.DataListener {
            override fun onSuccess(data: String) {
             response = data
             signal!!.countDown();
            }

            override fun onError() {
                TODO("Not yet implemented")
            }
        }).getData()
        signal!!.await()

        Assert.assertTrue(response.toString().contains("dailyData"))


    }

}
