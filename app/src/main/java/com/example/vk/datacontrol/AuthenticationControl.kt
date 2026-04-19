package com.example.vk.datacontrol

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    val auth = Firebase.auth
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _userLogin = MutableStateFlow<String?>(null)
    val userLogin: StateFlow<String?> = _userLogin
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail
    private val _passwordChangeState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Nothing)
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState

    init{
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val currentFlag = if (_authState.value is AuthState.Authenticated) {
                (_authState.value as AuthState.Authenticated).isJustRegistered
            } else false

            _authState.value = if (user != null) {
                AuthState.Authenticated(isJustRegistered = currentFlag)
            } else {
                AuthState.Unauthenticated
            }
        }
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _userLogin.value = currentUser.displayName ?: currentUser.email
            _userEmail.value = currentUser.email
        }
    }
    val authState: StateFlow<AuthState> = _authState
    fun signIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("Log","success sign in")
                    val user = auth.currentUser
                    _userLogin.value = user?.displayName ?: "Пользователь"
                    _userEmail.value = user?.email
                    _authState.value = AuthState.Authenticated(isJustRegistered = false)
                    _errorMessage.value = null
                    Log.d("Loga", email)
                    //Log.d("Loga", password)
                }else{
                    Log.d("Log","fail sihn in")
                    _errorMessage.value="Вы ошиблись с введенными данными"
                }
            }
    }

    private fun isPasswordValid(password: String): Boolean {
        val hasDigit = password.any { it.isDigit() }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasMinLength = password.length >= 8

        return hasMinLength && hasDigit && hasUpperCase
    }
    fun signUp(email:String, password:String,login:String){
        if (!isPasswordValid(password)) {
            _errorMessage.value = "Пароль должен быть не менее 8 символов, содержать цифру и заглавную букву"
            return
        }
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(login)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d("Loga", "Display name updated successfully")
                            _authState.value = AuthState.Authenticated(isJustRegistered = true)
                            _errorMessage.value = null
                            _userLogin.value = login
                            _userEmail.value = email
                        } else {
                            _authState.value = AuthState.Authenticated(isJustRegistered = true)
                            _errorMessage.value = "Аккаунт создан, но имя не сохранено"
                        }
                    }
                }else{
                    Log.d("Loga","fail")
                    _errorMessage.value="Неподходящие данные"
                }
            }
    }
    fun signOut() {
        auth.signOut()
        _userLogin.value = null
        _userEmail.value = null
        _authState.value = AuthState.Unauthenticated
        _errorMessage.value = null
    }
    fun clearError() {
        _errorMessage.value = null
    }
    fun clearJustRegistered() {
        _authState.value = AuthState.Authenticated(isJustRegistered = false)
    }
    fun changePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser ?: run {
            _passwordChangeState.value = PasswordChangeState.Error("Пользователь не найден")
            return
        }
        val email = user.email ?: run {
            _passwordChangeState.value = PasswordChangeState.Error("Email не найден")
            return
        }
        _passwordChangeState.value = PasswordChangeState.Loading
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (!reauthTask.isSuccessful) {
                    _passwordChangeState.value = PasswordChangeState.Error("Неверный текущий пароль")
                    //Log.d("Loga",currentPassword)
                    Log.d("Loga",email)
                    return@addOnCompleteListener
                }
                user.updatePassword(newPassword)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            _passwordChangeState.value = PasswordChangeState.Success("Пароль успешно изменён")
                        } else {
                            _passwordChangeState.value = PasswordChangeState.Error("Ошибка при смене пароля")
                        }
                    }
            }
    }
    fun resetPasswordChangeState() {
        _passwordChangeState.value = PasswordChangeState.Nothing
    }


}
sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val isJustRegistered: Boolean = false) : AuthState()
}
sealed class PasswordChangeState {
    object Nothing : PasswordChangeState()
    object Loading : PasswordChangeState()
    data class Success(val message: String) : PasswordChangeState()
    data class Error(val message: String) : PasswordChangeState()
}