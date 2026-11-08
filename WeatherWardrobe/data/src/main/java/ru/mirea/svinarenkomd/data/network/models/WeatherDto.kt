package ru.mirea.svinarenkomd.data.network.models

data class WeatherDto(
    val city: String,
    val temperature: Int,
    val condition: String,
    val windSpeed: Int
)
