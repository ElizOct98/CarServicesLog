package com.sperez.carserviceslog.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.CarServicesLogState
import com.sperez.carserviceslog.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class LogInViewModel: ViewModel() {
    private var _auth = Firebase.auth
    private var _userSignedIn = _auth.currentUser
    private var _currentState = mutableStateOf(
        if(_userSignedIn != null ){
            CarServicesLogState.Logged
        }else{
            CarServicesLogState.NotLogged
        }
    )
    val currentState : State<CarServicesLogState> = _currentState
    private var _events = MutableSharedFlow<CarServicesLogEvent>()


    init {

        viewModelScope.launch {
            _events.collect{
                when(it){
                    is CarServicesLogEvent.ForgotPassword -> resetPassword(it.user)
                    is CarServicesLogEvent.NewUser -> createUser(it.user,it.password)
                    is CarServicesLogEvent.SignIn -> signIn(it.user,it.password)
                    CarServicesLogEvent.SignOut -> signOut()
                    CarServicesLogEvent.NavigateForgotPassword -> _currentState.value = CarServicesLogState.ForgotPassword
                    CarServicesLogEvent.NavigateNewUser ->  _currentState.value = CarServicesLogState.CreateUser
                }
            }
        }
    }

    fun dispatchEventLogIn(event: CarServicesLogEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
    private fun resetPassword(user: String){
        _currentState.value = CarServicesLogState.Loading
        _auth.sendPasswordResetEmail(user).addOnCompleteListener {
            _currentState.value = CarServicesLogState.ResetPasswordSuccessful(R.string.reset_password_message)
        }.addOnFailureListener {
            _currentState.value = CarServicesLogState.Error(R.string.log_in_message_error)
        }
    }
    private fun createUser(user: String, password: String){
        _currentState.value = CarServicesLogState.Loading
        _auth.createUserWithEmailAndPassword(user,password).addOnCompleteListener {
            _currentState.value = CarServicesLogState.NotLogged
        }.addOnFailureListener {
            _currentState.value = CarServicesLogState.Error(R.string.log_in_message_error)
        }
    }
    private fun signIn(user: String, password: String){
        _currentState.value = CarServicesLogState.Loading
        _auth.signInWithEmailAndPassword(user,password).addOnCompleteListener {
            _currentState.value = CarServicesLogState.Logged
        }.addOnFailureListener {
            _currentState.value = CarServicesLogState.Error(R.string.log_in_message_error)
        }
    }
    private fun signOut(){
        _auth.signOut()
        _currentState.value = CarServicesLogState.NotLogged
    }
}