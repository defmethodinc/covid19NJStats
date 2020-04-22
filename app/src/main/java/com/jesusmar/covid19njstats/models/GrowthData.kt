package com.jesusmar.covid19njstats.models

data class ResponseDataGrowth(
    val dailyData: List<GrowthData>
)

data class GrowthData (
    val day: Int,
    val value: Double
)
