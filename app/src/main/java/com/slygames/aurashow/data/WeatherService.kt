package com.slygames.aurashow.data

import com.slygames.aurashow.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // or "imperial"
    ): WeatherResponse

    companion object {
        fun create(): WeatherService {
            return Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherService::class.java)
        }
    }
}
