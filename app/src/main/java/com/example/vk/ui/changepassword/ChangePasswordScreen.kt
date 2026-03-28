package com.example.vk.ui.changepassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.vk.R
import com.example.vk.datacontrol.AuthRepository
import com.example.vk.datacontrol.AuthViewModel
import com.example.vk.navigation.AppScreens
import com.example.vk.ui.components.fields.ChangePasswordInputField
import com.example.vk.ui.components.fields.PasswordInputField
import com.example.vk.ui.components.fields.TextInputField
import com.example.vk.ui.settings.SettingsPart
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.SignupBackground
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class PasswordChangeState {
    object Idle : PasswordChangeState()
    object Loading : PasswordChangeState()
    data class Success(val message: String) : PasswordChangeState()
    data class Error(val message: String) : PasswordChangeState()
}

class ChangePasswordViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Idle)
    val uiState = _uiState.asStateFlow()

    fun changePassword(oldPass: String, newPass: String, confirmPass: String) {
        when {
            oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank() -> {
                _uiState.value = PasswordChangeState.Error("Заполните все поля")
            }
            newPass != confirmPass -> {
                _uiState.value = PasswordChangeState.Error("Новые пароли не совпадают")
            }
            else -> {
                _uiState.value = PasswordChangeState.Loading
                repository.changePassword(oldPass, newPass) { success, message ->
                    if (success) _uiState.value = PasswordChangeState.Success(message ?: "Готово")
                    else _uiState.value = PasswordChangeState.Error(message ?: "Ошибка")
                }
            }
        }
    }

    fun resetState() { _uiState.value = PasswordChangeState.Idle }
}
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel
) {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordChangeState by viewModel.uiState.collectAsState()

    LaunchedEffect(passwordChangeState) {
        when (val state = passwordChangeState) {
            is PasswordChangeState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
                navController.popBackStack()
            }
            is PasswordChangeState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    val inputModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(SignupBackground)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text("Смена пароля", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(60.dp))

        ChangePasswordInputField(
            modifier = inputModifier,
            placeholder = "Старый пароль",
            value = currentPassword,
            onValueChange = { currentPassword = it },
        )
        Spacer(modifier = Modifier.height(16.dp))

        ChangePasswordInputField(
            modifier = inputModifier,
            placeholder = "Новый пароль",
            value = newPassword,
            onValueChange = { newPassword = it },
        )
        Spacer(modifier = Modifier.height(16.dp))

        ChangePasswordInputField(
            modifier = inputModifier,
            placeholder = "Повторный новый пароль",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
            },
            enabled = passwordChangeState != PasswordChangeState.Loading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeContinue
            )
        ) {
            if (passwordChangeState is PasswordChangeState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("Сменить пароль")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { navController.navigate(AppScreens.SettingsScreen.route) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeContinue
            )
        ) {
            Text("Отмена")
        }
    }
}