package ru.mirea.svinarenkomd.data.storage.models

data class UserPrefsModel(
    val email: String,
    val preferredCity: String,
    val isAuthorized: Boolean,
    val lastSyncDate: String
)
