package com.example.vk.datacontrol

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    val auth = Firebase.auth
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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
    }
    val authState: StateFlow<AuthState> = _authState
    fun signIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("Log","success sign in")
                    _authState.value = AuthState.Authenticated(isJustRegistered = false)
                    _errorMessage.value=null
                }else{
                    Log.d("Log","fail sihn in")
                    _errorMessage.value="Вы ошиблись с введенными данными"
                }
            }
    }
    fun signUp(email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("Log","success")
                    _authState.value = AuthState.Authenticated(isJustRegistered = true)
                    _errorMessage.value=null
                }else{
                    Log.d("Log","fail")
                    _errorMessage.value="Неподходящие данные"
                }
            }
    }
    fun signOut() {
        auth.signOut()
    }
    fun clearError() {
        _errorMessage.value = null
    }
    fun clearJustRegistered() {
        _authState.value = AuthState.Authenticated(isJustRegistered = false)
    }


}
sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val isJustRegistered: Boolean = false) : AuthState()
}