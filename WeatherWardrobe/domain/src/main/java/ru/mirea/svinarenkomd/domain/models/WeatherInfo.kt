package ru.mirea.svinarenkomd.domain.models

data class WeatherInfo(
    val city: String,
    val temperature: Int,
    val condition: String,
    val windSpeed: Int,
    val recommendation: String
)
