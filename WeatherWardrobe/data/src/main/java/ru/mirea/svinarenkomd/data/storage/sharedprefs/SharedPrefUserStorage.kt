package ru.mirea.svinarenkomd.data.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import ru.mirea.svinarenkomd.data.storage.UserStorage
import ru.mirea.svinarenkomd.data.storage.models.UserPrefsModel
import java.time.LocalDate

class SharedPrefUserStorage(context: Context) : UserStorage {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getUserPrefs(): UserPrefsModel {
        val email = sharedPreferences.getString(KEY_EMAIL, "Гость") ?: "Гость"
        val city = sharedPreferences.getString(KEY_CITY, "Москва") ?: "Москва"
        val isAuthorized = sharedPreferences.getBoolean(KEY_AUTHORIZED, false)
        val lastSync = sharedPreferences.getString(KEY_LAST_SYNC, LocalDate.now().toString())
            ?: LocalDate.now().toString()
        return UserPrefsModel(
            email = email,
            preferredCity = city,
            isAuthorized = isAuthorized,
            lastSyncDate = lastSync
        )
    }

    override fun saveAuthUser(email: String, isAuthorized: Boolean): Boolean {
        sharedPreferences.edit()
            .putString(KEY_EMAIL, email)
            .putBoolean(KEY_AUTHORIZED, isAuthorized)
            .putString(KEY_LAST_SYNC, LocalDate.now().toString())
            .apply()
        return true
    }

    override fun savePreferredCity(city: String): Boolean {
        sharedPreferences.edit()
            .putString(KEY_CITY, city)
            .putString(KEY_LAST_SYNC, LocalDate.now().toString())
            .apply()
        return true
    }

    override fun clearAuth() {
        sharedPreferences.edit()
            .putString(KEY_EMAIL, "Гость")
            .putBoolean(KEY_AUTHORIZED, false)
            .putString(KEY_LAST_SYNC, LocalDate.now().toString())
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "weather_wardrobe_user_prefs"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_CITY = "preferred_city"
        private const val KEY_AUTHORIZED = "is_authorized"
        private const val KEY_LAST_SYNC = "last_sync_date"
    }
}
