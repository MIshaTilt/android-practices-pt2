package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    fun execute() {
        authRepository.logout()
    }
}
