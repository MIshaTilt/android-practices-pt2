package ru.mirea.svinarenkomd.weatherwardrobe.data.repository

import android.content.Context
import android.content.SharedPreferences
import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.UserProfile
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.UserRepository

class UserRepositoryImpl(context: Context) : UserRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(AuthRepositoryImpl.PREFS_NAME, Context.MODE_PRIVATE)

    override fun getUserProfile(): UserProfile {
        val email = prefs.getString(AuthRepositoryImpl.KEY_EMAIL, "student@mirea.ru") ?: "student@mirea.ru"
        val city = prefs.getString(AuthRepositoryImpl.KEY_CITY, "Москва") ?: "Москва"
        val isAuth = prefs.getBoolean(AuthRepositoryImpl.KEY_AUTHORIZED, true)
        return UserProfile(
            email = email,
            preferredCity = city,
            isAuthorized = isAuth
        )
    }

    override fun savePreferredCity(city: String): Boolean {
        prefs.edit()
            .putString(AuthRepositoryImpl.KEY_CITY, city)
            .apply()
        return true
    }
}
