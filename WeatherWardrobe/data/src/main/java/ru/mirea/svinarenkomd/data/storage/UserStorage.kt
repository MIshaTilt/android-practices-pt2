package ru.mirea.svinarenkomd.data.storage

import ru.mirea.svinarenkomd.data.storage.models.UserPrefsModel

interface UserStorage {
    fun getUserPrefs(): UserPrefsModel
    fun saveAuthUser(email: String, isAuthorized: Boolean): Boolean
    fun savePreferredCity(city: String): Boolean
    fun clearAuth()
}
