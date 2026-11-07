package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.WeatherInfo
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(city: String): WeatherInfo {
        val targetCity = if (city.isBlank()) "Москва" else city.trim()
        return weatherRepository.getWeather(targetCity)
    }
}
