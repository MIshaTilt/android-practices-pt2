package ru.mirea.svinarenkomd.domain.models

data class UserProfile(
    val email: String,
    val preferredCity: String,
    val isAuthorized: Boolean
)
