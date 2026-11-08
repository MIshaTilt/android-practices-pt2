package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.WeatherInfo
import ru.mirea.svinarenkomd.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    fun execute(city: String): WeatherInfo {
        return weatherRepository.getCurrentWeather(city)
    }
}
