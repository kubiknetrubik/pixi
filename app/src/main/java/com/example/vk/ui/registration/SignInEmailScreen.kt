package com.example.vk.ui.registration

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.vk.R
import com.example.vk.datacontrol.AuthRepository
import com.example.vk.datacontrol.AuthViewModel
import com.example.vk.navigation.AppScreens
import com.example.vk.ui.components.buttons.ContinueButton
import com.example.vk.ui.components.fields.PasswordInputField
import com.example.vk.ui.components.fields.TextInputField
import com.example.vk.ui.theme.BrownText
import com.example.vk.ui.theme.Error
import com.example.vk.ui.theme.OrangePrimary
import com.example.vk.ui.theme.SignupBackground
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        repository.signIn(email, password) { success, error ->
            if (success) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = error) }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}
@Composable
fun SignInEmailScreen(
    navController: NavController,
    onNavigateToEmail: () -> Unit,
    viewModel: SignInViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(AppScreens.FirstEntryScreen.route) {
                popUpTo(AppScreens.SignUpScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SignupBackground).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(painter = painterResource(id = R.drawable.main_logo), contentDescription = "Logo", modifier = Modifier.size(282.dp))
        Spacer(modifier = Modifier.height(24.dp))

        val inputModifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

        TextInputField(modifier = inputModifier, placeholder = "Почта", value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.height(12.dp))

        PasswordInputField(
            modifier = inputModifier, placeholder = "Пароль", value = password,
            onValueChange = { password = it }, passwordVisible = passwordVisible, onclickpass = { passwordVisible = !passwordVisible }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Еще нет аккаунта? Зарегистрируйтесь", color = BrownText, textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onNavigateToEmail() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = OrangePrimary)
        } else {
            ContinueButton(onClick = { viewModel.signIn(email, password) })
        }
    }
}
data class SignInUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)