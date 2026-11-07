package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.UserProfile
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): UserProfile {
        return userRepository.getUserProfile()
    }
}
