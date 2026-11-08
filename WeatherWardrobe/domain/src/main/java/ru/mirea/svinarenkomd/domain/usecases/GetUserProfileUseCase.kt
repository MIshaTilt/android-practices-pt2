package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.UserProfile
import ru.mirea.svinarenkomd.domain.repository.UserRepository

class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): UserProfile {
        return userRepository.getUserProfile()
    }
}
