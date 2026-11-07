package ru.mirea.svinarenkomd.weatherwardrobe.data.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.WeatherInfo
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.WeatherRepository

class WeatherRepositoryImpl : WeatherRepository {

    override fun getWeather(city: String): WeatherInfo {
        return when (city.lowercase()) {
            "санкт-петербург", "спб" -> WeatherInfo(
                city = "Санкт-Петербург",
                temperature = 11,
                condition = "Небольшой дождь, облачно",
                windSpeed = 7
            )
            "сочи" -> WeatherInfo(
                city = "Сочи",
                temperature = 24,
                condition = "Ясно, солнечно",
                windSpeed = 3
            )
            "новосибирск" -> WeatherInfo(
                city = "Новосибирск",
                temperature = -8,
                condition = "Снег, морозно",
                windSpeed = 5
            )
            else -> WeatherInfo(
                city = city.ifBlank { "Москва" },
                temperature = 15,
                condition = "Переменная облачность",
                windSpeed = 4
            )
        }
    }
}
