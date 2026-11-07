package ru.mirea.svinarenkomd.weatherwardrobe.data.repository

import android.content.Context
import android.content.SharedPreferences
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.AuthRepository

class AuthRepositoryImpl(context: Context) : AuthRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun login(email: String, password: String): Boolean {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putBoolean(KEY_AUTHORIZED, true)
            .apply()
        return true
    }

    override fun register(email: String, password: String): Boolean {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putBoolean(KEY_AUTHORIZED, true)
            .apply()
        return true
    }

    override fun logout() {
        prefs.edit()
            .putString(KEY_EMAIL, "Гость")
            .putBoolean(KEY_AUTHORIZED, false)
            .apply()
    }

    companion object {
        const val PREFS_NAME = "weather_wardrobe_prefs"
        const val KEY_EMAIL = "user_email"
        const val KEY_CITY = "preferred_city"
        const val KEY_AUTHORIZED = "is_authorized"
    }
}
