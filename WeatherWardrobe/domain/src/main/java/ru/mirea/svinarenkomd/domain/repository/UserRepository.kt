package ru.mirea.svinarenkomd.domain.repository

import ru.mirea.svinarenkomd.domain.models.UserProfile

interface UserRepository {
    fun getUserProfile(): UserProfile
    fun savePreferredCity(city: String): Boolean
}
