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
import androidx.navigation.NavController
import com.example.vk.R
import com.example.vk.datacontrol.AuthViewModel
import com.example.vk.datacontrol.PasswordChangeState
import com.example.vk.navigation.AppScreens
import com.example.vk.ui.components.fields.ChangePasswordInputField
import com.example.vk.ui.components.fields.PasswordInputField
import com.example.vk.ui.components.fields.TextInputField
import com.example.vk.ui.settings.SettingsPart
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.SignupBackground


@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val inputModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    val passwordChangeState by authViewModel.passwordChangeState.collectAsState()

    LaunchedEffect(passwordChangeState) {
        when (val state = passwordChangeState) {
            is PasswordChangeState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetPasswordChangeState()
                navController.popBackStack()
            }
            is PasswordChangeState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetPasswordChangeState()
            }
            else -> {}
        }
    }
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
                when {
                    currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() ->
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    newPassword != confirmPassword ->
                        Toast.makeText(context, "Новые пароли не совпадают", Toast.LENGTH_SHORT).show()
                    else ->
                        authViewModel.changePassword(currentPassword, newPassword)

                }

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeContinue
            ),
            enabled = passwordChangeState != PasswordChangeState.Loading
        ) {
            if (passwordChangeState is PasswordChangeState.Loading) {
                CircularProgressIndicator(color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,modifier = Modifier.size(16.dp))
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