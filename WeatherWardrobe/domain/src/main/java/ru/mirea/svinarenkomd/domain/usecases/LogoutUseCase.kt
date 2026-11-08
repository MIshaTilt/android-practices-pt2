package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    fun execute() {
        authRepository.logout()
    }
}
