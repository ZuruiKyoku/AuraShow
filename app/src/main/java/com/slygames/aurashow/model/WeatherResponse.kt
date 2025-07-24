package com.slygames.aurashow.model

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String,
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float
)
