package com.example.vk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vk.ui.main.FirstEntryScreen
import com.example.vk.ui.registration.AppleRegistrationScreen
import com.example.vk.ui.registration.EmailRegistrationScreen
import com.example.vk.ui.registration.GoogleRegistrationScreen
import com.example.vk.ui.registration.VkRegistrationScreen
import com.example.vk.ui.signup.SignUpScreen
import com.example.vk.ui.welcome.WelcomeScreen
import com.example.vk.ui.settings.SettingsScreen
import android.util.Log
import com.example.vk.data.MainDb
import com.example.vk.data.NoteRepository
import com.example.vk.ui.general.GeneralScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import com.example.vk.data.BalanceRepository
import com.example.vk.datacontrol.AuthRepository
import com.example.vk.datacontrol.AuthState
import com.example.vk.datacontrol.AuthViewModel
import com.example.vk.ui.changepassword.ChangePasswordScreen
import com.example.vk.ui.changepassword.ChangePasswordViewModel
import com.example.vk.ui.general.GeneralViewModel

import com.example.vk.ui.main.TaskViewModel
import com.example.vk.ui.registration.SignInEmailScreen
import com.example.vk.ui.registration.SignInViewModel
import com.example.vk.ui.registration.SignUpViewModel
import com.example.vk.ui.settings.ProfileViewModel

@Composable

fun NavGraph(navController: NavHostController,
             balanceRepository: BalanceRepository,
authRepository: AuthRepository
)
{

    val authvm: AuthViewModel = viewModel(
        initializer = { AuthViewModel()}
    )

    val authState by authvm.authState.collectAsState()
    val startDestination = when (authState) {
        is AuthState.Unauthenticated -> AppScreens.SignUpScreen.route
        is AuthState.Authenticated -> {
            if ((authState as AuthState.Authenticated).isJustRegistered) {
                AppScreens.WelcomeScreen.route
            } else {
                AppScreens.FirstEntryScreen.route
            }
        }
    }

    /*val startDestination = remember {
        if (authRepository.currentUser != null) AppScreens.FirstEntryScreen.route
        else AppScreens.SignUpScreen.route
    }*/


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(route = AppScreens.SignUpScreen.route) {

            SignUpScreen(
                onNavigateToApple = {
                    navController.navigate(AppScreens.AppleRegistrationScreen.route)
                },
                onNavigateToGoogle = {
                    navController.navigate(AppScreens.GoogleRegistrationScreen.route)
                },
                onNavigateToVk = {
                    navController.navigate(AppScreens.VkRegistrationScreen.route)
                },
                onNavigateToEmail = {
                    navController.navigate(AppScreens.EmailRegistrationScreen.route)
                },
                onSkipClick = {
                    navController.navigate(AppScreens.WelcomeScreen.route)
                },
                onSignInClick= {
                    navController.navigate(AppScreens.SignInEmailScreen.route)
                }
            )
        }
        composable(route = AppScreens.SignInEmailScreen.route) {
            val vm: SignInViewModel = viewModel(
                initializer = {
                    SignInViewModel(authRepository)
                }
            )
            SignInEmailScreen(navController = navController,onNavigateToEmail ={
                navController.navigate(AppScreens.EmailRegistrationScreen.route)
            },viewModel=vm)
        }




        composable(route = AppScreens.GeneralScreen.route) {
            val generalViewModel: GeneralViewModel = viewModel(
                initializer = {
                    GeneralViewModel(balanceRepository)
                }
            )
            GeneralScreen(
                viewModel = generalViewModel,
                onNavigatetoSettings = { navController.navigate(AppScreens.SettingsScreen.route) },
                onNavigatetoTasks = { navController.navigate(AppScreens.FirstEntryScreen.route) },
                onNavigatetoShop = { navController.navigate(AppScreens.GeneralScreen.route) }
            )
        }

        composable(route = AppScreens.AppleRegistrationScreen.route) {
            AppleRegistrationScreen(navController = navController)
        }

        composable(route = AppScreens.GoogleRegistrationScreen.route) {
            GoogleRegistrationScreen(navController = navController)
        }

        composable(route = AppScreens.VkRegistrationScreen.route) {
            VkRegistrationScreen(navController = navController)
        }

        composable(route = AppScreens.EmailRegistrationScreen.route) {
            val vm: SignUpViewModel = viewModel(
                initializer = {
                    SignUpViewModel(authRepository)
                }
            )
            EmailRegistrationScreen(navController = navController, onSignInClick = {
                navController.navigate(AppScreens.SignInEmailScreen.route)
            },viewModel=vm)
        }

        composable(route = AppScreens.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }

        composable(route = AppScreens.WelcomeScreenEmail.route) {backStackEntry ->

            val login = backStackEntry.arguments?.getString("login")
            val email = backStackEntry.arguments?.getString("email")
            val password = backStackEntry.arguments?.getString("password")

        
            WelcomeScreen(navController = navController,login,email,password)

        }



        composable(route = AppScreens.FirstEntryScreen.route) {
            val context = LocalContext.current
            val vm: TaskViewModel = viewModel(
                initializer = {
                    val dao = MainDb.createDataBase(context).dao
                    TaskViewModel(NoteRepository(dao, balanceRepository ), balanceRepository)
                }
            )
            FirstEntryScreen(
                vm,
                onNavigatetoSettings = { navController.navigate(AppScreens.SettingsScreen.route) },
                onNavigatetoShop = {navController.navigate(AppScreens.GeneralScreen.route)}

            )
        }
        composable(route = AppScreens.FirstEntryScreenEmail.route) {backStackEntry ->
            val context = LocalContext.current
            val vm: TaskViewModel = viewModel(
                initializer = {
                    val dao = MainDb.createDataBase(context).dao
                    TaskViewModel(NoteRepository(dao, balanceRepository ), balanceRepository)
                }
            )
            val login = backStackEntry.arguments?.getString("login")
            val email = backStackEntry.arguments?.getString("email")
            val password = backStackEntry.arguments?.getString("password")
            FirstEntryScreen(
                vm,
                onNavigatetoSettings = { navController.navigate("settings/$login/$email/$password") },
                onNavigatetoShop = { navController.navigate(AppScreens.GeneralScreen.route) }
            )
        }
        composable(route = AppScreens.SettingsScreenEmail.route){backStackEntry ->
            val login = backStackEntry.arguments?.getString("login")
            val email = backStackEntry.arguments?.getString("email")
            val password = backStackEntry.arguments?.getString("password")
            val vm: ProfileViewModel = viewModel(
                initializer = {
                    ProfileViewModel(authRepository)
                }
            )
            SettingsScreen(
                onNavigatetoTasks = { navController.navigate(AppScreens.FirstEntryScreen.route) },
                onNavigatetoShop =  {navController.navigate(AppScreens.GeneralScreen.route)},
                onNavigatetoChange = { navController.navigate(AppScreens.ChangePasswordScreen.route) },
                navController = navController,
                viewModel = vm
            )

        }
        composable(route = AppScreens.SettingsScreen.route){
            val vm: ProfileViewModel = viewModel(
                initializer = {
                    ProfileViewModel(authRepository)
                }
            )
            SettingsScreen(
                onNavigatetoTasks = { navController.navigate(AppScreens.FirstEntryScreen.route) },
                onNavigatetoShop =  {navController.navigate(AppScreens.GeneralScreen.route)},
                onNavigatetoChange = { navController.navigate(AppScreens.ChangePasswordScreen.route) },
                navController = navController,
                viewModel = vm
            )
        }
        composable(route = AppScreens.ChangePasswordScreen.route){
            val vm: ChangePasswordViewModel = viewModel(
                initializer = {
                    ChangePasswordViewModel(authRepository)
                }
            )
            ChangePasswordScreen(navController,viewModel=vm)

        }
    }
}
