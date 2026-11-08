package ru.mirea.svinarenkomd.domain.repository

interface AuthRepository {
    fun login(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)? = null
    ): Boolean

    fun register(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)? = null
    ): Boolean

    fun logout()
}
