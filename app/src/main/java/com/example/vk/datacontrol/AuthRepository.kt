package com.example.vk.datacontrol

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepository(private val auth: FirebaseAuth = Firebase.auth) {

    fun getAuthState(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { fbAuth ->
            trySend(fbAuth.currentUser != null)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    val currentUser get() = auth.currentUser

    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onComplete(true, null)
                else onComplete(false, "Вы ошиблись с введенными данными")
            }
    }

    fun signUp(email: String, password: String, login: String, onComplete: (Boolean, String?) -> Unit) {
        Log.d("Loga",password)
        Log.d("Loga",email)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(login)
                        .build()
                    auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        onComplete(true, null)
                    }
                } else {
                    onComplete(false, "Неподходящие данные")
                }
            }
    }

    fun changePassword(currentPassword: String, newPassword: String, onComplete: (Boolean, String?) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false, "Пользователь не найден")
        val email = user.email ?: return onComplete(false, "Email не найден")

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (!reauthTask.isSuccessful) {
                return@addOnCompleteListener onComplete(false, "Неверный текущий пароль")
            }
            user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) onComplete(true, null)
                else onComplete(false, "Ошибка при смене пароля")
            }
        }
    }

    fun signOut() = auth.signOut()
}