package ru.mirea.svinarenkomd.weatherwardrobe.domain.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.WeatherInfo

interface WeatherRepository {
    fun getWeather(city: String): WeatherInfo
}
