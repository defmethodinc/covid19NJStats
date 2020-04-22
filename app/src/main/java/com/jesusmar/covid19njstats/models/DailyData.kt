package com.jesusmar.covid19njstats.models
import java.util.*

data class ResponseData(
    val dailyData: List<DailyData>
)

data class DailyData (
    val id: Int,
    val date: Date,
    val owner: String,
    val positive: Int,
    val negative: Int,
    val deaths: Int
)
