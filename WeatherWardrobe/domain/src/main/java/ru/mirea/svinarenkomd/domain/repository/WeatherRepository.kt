package ru.mirea.svinarenkomd.domain.repository

import ru.mirea.svinarenkomd.domain.models.WeatherInfo

interface WeatherRepository {
    fun getCurrentWeather(city: String): WeatherInfo
}
