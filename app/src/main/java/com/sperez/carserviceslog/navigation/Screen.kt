package com.sperez.carserviceslog.navigation

sealed class Screen(val route: String) {
    data object Login: Screen("log_in_screen")
    data object NewUser: Screen("new_user_screen")
    data object ForgotPassword: Screen("forgot_password_screen")
    data object ServicesLog: Screen("services_screen")
    data object NewServiceLog: Screen("new_service_log")
}