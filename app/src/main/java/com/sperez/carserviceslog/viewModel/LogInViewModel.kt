package com.sperez.carserviceslog.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sperez.carserviceslog.AuthRepository
import com.sperez.carserviceslog.AuthResult
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.R
import com.sperez.carserviceslog.Screen
import com.sperez.carserviceslog.ViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class LogInViewModel : ViewModel() {

    private var authRepository = AuthRepository()
    private var _currentState = mutableStateOf(ViewState())

    val currentState : State<ViewState> = _currentState
    private var _events = MutableSharedFlow<CarServicesLogEvent>()
    private lateinit var navController: NavController

    init {
        viewModelScope.launch {
            _events.collect {
                when(it){
                    is CarServicesLogEvent.ForgotPassword -> resetPassword(it.user)
                    is CarServicesLogEvent.NewUser -> createUser(it.user,it.password)
                    is CarServicesLogEvent.SignIn -> signIn(it.user,it.password)
                    CarServicesLogEvent.SignOut -> signOut()
                    CarServicesLogEvent.NavigateForgotPassword -> navController.navigate(Screen.ForgotPassword.route)
                    CarServicesLogEvent.NavigateNewUser ->  navController.navigate(Screen.NewUser)
                    CarServicesLogEvent.DisplayLogs -> {}
                    CarServicesLogEvent.NavigateNewLog -> {}
                    is CarServicesLogEvent.NewServiceLog -> {}
                }
            }
        }
    }

    fun onCreate(navController: NavController) {
        this.navController = navController
    }

    fun dispatchEvent(event: CarServicesLogEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    fun clearMessages() {
        _currentState.value = _currentState.value.copy(errorMessage = null, successMessage = null)
    }

    fun isUserLogged() = authRepository.isUserLogged()

    private fun resetPassword(user: String){
        _currentState.value = _currentState.value.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.resetPassword(user)

            if (result is AuthResult.Completed) {
                _currentState.value = _currentState.value.copy(successMessage = R.string.reset_password_message)
            } else if (result is AuthResult.Error) {
                _currentState.value = _currentState.value.copy(errorMessage = result.message)
            }

            _currentState.value = _currentState.value.copy(isLoading = false)
        }
    }

    private fun createUser(user: String, password: String){
        _currentState.value = _currentState.value.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.createUser(user, password)

            if (result is AuthResult.Completed) {
                navController.popBackStack()
                _currentState.value = _currentState.value.copy(successMessage = R.string.user_register_success)
            } else if (result is AuthResult.Error) {
                _currentState.value = _currentState.value.copy(errorMessage = result.message)
            }

            _currentState.value = _currentState.value.copy(isLoading = false)
        }
    }

    private fun signIn(user: String, password: String){
        _currentState.value = _currentState.value.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.signIn(user, password)

            if (result is AuthResult.SignInSuccess) {
                navController.navigate(Screen.ServicesLog.route)
            } else if (result is AuthResult.Error) {
                _currentState.value = _currentState.value.copy(errorMessage = result.message)
            }

            _currentState.value = _currentState.value.copy(isLoading = false)
        }
    }

    private fun signOut() {
        authRepository.signOut()
        navController.navigate(Screen.Login.route)
    }
}