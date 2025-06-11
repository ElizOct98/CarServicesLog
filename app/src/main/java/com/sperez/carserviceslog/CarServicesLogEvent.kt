package com.sperez.carserviceslog

sealed class CarServicesLogEvent {
    data class SignIn(val user: String, val password: String) : CarServicesLogEvent()
    data object SignOut : CarServicesLogEvent()
    data class NewUser(val user: String,val password: String) : CarServicesLogEvent()
    data class ForgotPassword(val user: String) : CarServicesLogEvent()
    data object NavigateNewUser: CarServicesLogEvent()
    data object NavigateForgotPassword: CarServicesLogEvent()
}

sealed class CarServicesLogState{
    data object NotLogged : CarServicesLogState()
    data object Logged : CarServicesLogState()
    data object CreateUser : CarServicesLogState()
    data object ForgotPassword : CarServicesLogState()
    data object Loading : CarServicesLogState()

    data class Error(val message: Int) : CarServicesLogState()
    data class ResetPasswordSuccessful(val message: Int): CarServicesLogState()
}