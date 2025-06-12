package com.sperez.carserviceslog.viewModel

import android.os.Bundle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.sperez.carserviceslog.R
import com.sperez.carserviceslog.model.ServicesLog
import com.sperez.carserviceslog.navigation.Screen
import com.sperez.carserviceslog.repository.AuthRepository
import com.sperez.carserviceslog.repository.AuthResult
import com.sperez.carserviceslog.repository.DataRepository
import com.sperez.carserviceslog.repository.DataResult
import com.sperez.carserviceslog.view.CarServicesLogEvent
import com.sperez.carserviceslog.view.ViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CarServicesLogViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val dataRepository = DataRepository()
    lateinit var analytics: FirebaseAnalytics
    private var _currentState = mutableStateOf(ViewState())
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    val currentState : State<ViewState> = _currentState
    private var _events = MutableSharedFlow<CarServicesLogEvent>()

    private val _logs = mutableStateOf(listOf<ServicesLog>())
    val logs : State<List<ServicesLog>> = _logs

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
                    CarServicesLogEvent.DisplayLogs -> retrieveLogs()
                    CarServicesLogEvent.NavigateNewLog -> navController.navigate(Screen.NewServiceLog.route)
                    is CarServicesLogEvent.NewServiceLog -> addLog(it.newService)
                }
            }
        }
    }

    fun onCreate(navController: NavController,analytics: FirebaseAnalytics) {
        this.navController = navController
        this.analytics = analytics
        firebaseAnalytics = Firebase.analytics
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
                logSigInEvent()
                navController.navigate(Screen.ServicesLog.route)
            } else if (result is AuthResult.Error) {
                _currentState.value = _currentState.value.copy(errorMessage = result.message)
            }

            _currentState.value = _currentState.value.copy(isLoading = false)
        }
    }
    private fun logSigInEvent(){
        val currentUser = authRepository.getCurrentUser()
        currentUser?.uid?.let{ userId ->
            val params = Bundle().apply{
                putString("user_id", userId)
            }
            analytics.logEvent("user_login", params)
        }
    }

    private fun signOut() {
        authRepository.signOut()
        navController.navigate(Screen.Login.route)
    }

    private fun retrieveLogs() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser != null) {
                val result = dataRepository.getUserLogs(currentUser.uid)
                if (result is DataResult.GetDataSuccess) {
                    _logs.value = result.logs
                } else {
                    _currentState.value = _currentState.value.copy(errorMessage = R.string.log_in_message_error)
                }
            }
        }
    }

    private fun addLog(log: ServicesLog) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser != null) {
                val result = dataRepository.addNewLog(currentUser.uid, log)
                if (result is DataResult.Completed) {
                    _currentState.value = _currentState.value.copy(successMessage = R.string.new_service_success)
                    navController.popBackStack()
                } else {
                    _currentState.value = _currentState.value.copy(errorMessage = R.string.log_in_message_error)
                }
            }
        }
    }
}