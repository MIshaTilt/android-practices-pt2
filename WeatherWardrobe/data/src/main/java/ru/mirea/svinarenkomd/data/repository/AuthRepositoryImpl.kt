package ru.mirea.svinarenkomd.data.repository

import com.google.firebase.auth.FirebaseAuth
import ru.mirea.svinarenkomd.data.storage.UserStorage
import ru.mirea.svinarenkomd.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userStorage: UserStorage
) : AuthRepository {

    private val firebaseAuth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (_: Exception) {
        null
    }

    override fun login(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)?
    ): Boolean {
        val auth = firebaseAuth
        if (auth == null) {
            userStorage.saveAuthUser(email, true)
            onResult?.invoke(true, "Локальный вход выполнен: $email")
            return true
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val resolvedEmail = user?.email ?: email
                    userStorage.saveAuthUser(resolvedEmail, true)
                    onResult?.invoke(
                        true,
                        "Firebase Auth: вход выполнен ($resolvedEmail, UID: ${user?.uid})"
                    )
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: "Ошибка авторизации Firebase"
                    onResult?.invoke(false, "Ошибка входа: $errorMsg")
                }
            }
        return true
    }

    override fun register(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)?
    ): Boolean {
        val auth = firebaseAuth
        if (auth == null) {
            userStorage.saveAuthUser(email, true)
            onResult?.invoke(true, "Локальная регистрация выполнена: $email")
            return true
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val resolvedEmail = user?.email ?: email
                    userStorage.saveAuthUser(resolvedEmail, true)
                    onResult?.invoke(
                        true,
                        "Firebase Auth: аккаунт создан ($resolvedEmail, UID: ${user?.uid})"
                    )
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: "Ошибка регистрации Firebase"
                    onResult?.invoke(false, "Ошибка регистрации: $errorMsg")
                }
            }
        return true
    }

    override fun logout() {
        try {
            firebaseAuth?.signOut()
        } catch (_: Exception) {
        }
        userStorage.clearAuth()
    }
}
