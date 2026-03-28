package com.example.vk.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.OrangePrimary
import com.example.vk.ui.theme.SignupBackground
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        val user = repository.currentUser
        _uiState.update { it.copy(
            login = user?.displayName ?: "Пользователь",
            email = user?.email ?: ""
        )}
    }

    fun logout() {
        repository.signOut()
        _uiState.update { it.copy(isLoggedOut = true) }
    }
}
@Composable
fun SettingsPart(text:String){
    Row(
        modifier= Modifier
            .border(2.dp,OrangePrimary, RoundedCornerShape(10.dp))
            .height(53.dp)
            .width(400.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun SettingsScreen(
    onNavigatetoTasks: () -> Unit,
    onNavigatetoShop: () -> Unit,
    onNavigatetoChange: () -> Unit,
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) {
            navController.navigate(AppScreens.SignUpScreen.route) {
                popUpTo(0)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SignupBackground)) {
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("Профиль", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(32.dp))

            Text("Логин", fontSize = 14.sp)
            SettingsPart(state.login)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Email", fontSize = 14.sp)
            SettingsPart(state.email)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Пароль", fontSize = 14.sp)
            SettingsPart("********")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onNavigatetoChange, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(OrangeContinue)) {
                Text("Сменить пароль")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.logout() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(OrangeContinue)) {
                Text("Выйти из аккаунта")
            }
        }
        BottomBar(onNavigatetoTasks = onNavigatetoTasks, onNavigatetoShop = onNavigatetoShop)
    }
}
data class ProfileUiState(
    val login: String = "",
    val email: String = "",
    val isLoggedOut: Boolean = false
)