package ru.mirea.svinarenkomd.weatherwardrobe.domain.models

data class WeatherInfo(
    val city: String,
    val temperature: Int,
    val condition: String,
    val windSpeed: Int
)
