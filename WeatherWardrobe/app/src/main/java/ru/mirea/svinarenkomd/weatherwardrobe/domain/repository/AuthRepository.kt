package ru.mirea.svinarenkomd.weatherwardrobe.domain.repository

interface AuthRepository {
    fun login(email: String, password: String): Boolean
    fun register(email: String, password: String): Boolean
    fun logout()
}
