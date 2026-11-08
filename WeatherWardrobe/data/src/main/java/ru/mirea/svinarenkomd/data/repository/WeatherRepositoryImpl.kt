package ru.mirea.svinarenkomd.data.repository

import ru.mirea.svinarenkomd.data.mappers.toDomain
import ru.mirea.svinarenkomd.data.network.NetworkApi
import ru.mirea.svinarenkomd.domain.models.WeatherInfo
import ru.mirea.svinarenkomd.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val networkApi: NetworkApi
) : WeatherRepository {

    override fun getCurrentWeather(city: String): WeatherInfo {
        val dto = networkApi.fetchWeather(city)
        val recommendation = when {
            dto.temperature < 0 -> "Морозно: рекомендуется зимний пуховик и тёплая обувь."
            dto.temperature in 0..10 -> "Холодно: наденьте пальто или утеплённую куртку."
            dto.temperature in 11..18 -> "Прохладно: подойдут мембранная ветровка, худи и джинсы."
            else -> "Тепло: отличная погода для лёгкой футболки и кед."
        }
        return dto.toDomain(recommendation)
    }
}
