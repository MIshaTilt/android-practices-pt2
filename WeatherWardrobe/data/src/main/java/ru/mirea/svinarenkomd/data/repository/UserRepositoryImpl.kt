package ru.mirea.svinarenkomd.data.repository

import com.google.firebase.auth.FirebaseAuth
import ru.mirea.svinarenkomd.data.mappers.toDomain
import ru.mirea.svinarenkomd.data.storage.UserStorage
import ru.mirea.svinarenkomd.domain.models.UserProfile
import ru.mirea.svinarenkomd.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userStorage: UserStorage
) : UserRepository {

    override fun getUserProfile(): UserProfile {
        val firebaseUser = try {
            FirebaseAuth.getInstance().currentUser
        } catch (_: Exception) {
            null
        }
        if (firebaseUser != null && !firebaseUser.email.isNullOrBlank()) {
            userStorage.saveAuthUser(firebaseUser.email!!, true)
        }
        return userStorage.getUserPrefs().toDomain()
    }

    override fun savePreferredCity(city: String): Boolean {
        return userStorage.savePreferredCity(city)
    }
}
