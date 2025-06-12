package com.sperez.carserviceslog.view

import com.sperez.carserviceslog.model.ServicesLog

sealed class CarServicesLogEvent {
    data class SignIn(val user: String, val password: String) : CarServicesLogEvent()
    data object SignOut : CarServicesLogEvent()
    data class NewUser(val user: String,val password: String) : CarServicesLogEvent()
    data class ForgotPassword(val user: String) : CarServicesLogEvent()
    data object NavigateNewUser: CarServicesLogEvent()
    data object NavigateForgotPassword: CarServicesLogEvent()
    data object DisplayLogs : CarServicesLogEvent()
    data object NavigateNewLog: CarServicesLogEvent()
    data class NewServiceLog(val newService: ServicesLog) : CarServicesLogEvent()
}

data class ViewState(
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val successMessage: Int? = null,
)