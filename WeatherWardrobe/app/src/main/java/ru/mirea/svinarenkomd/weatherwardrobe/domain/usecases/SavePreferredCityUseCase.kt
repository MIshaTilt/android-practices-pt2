package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.UserRepository

class SavePreferredCityUseCase(
    private val userRepository: UserRepository
) {
    fun execute(city: String): Boolean {
        if (city.isBlank()) {
            return false
        }
        return userRepository.savePreferredCity(city.trim())
    }
}
