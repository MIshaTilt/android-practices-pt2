package ru.mirea.svinarenkomd.weatherwardrobe.domain.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.UserProfile

interface UserRepository {
    fun getUserProfile(): UserProfile
    fun savePreferredCity(city: String): Boolean
}
