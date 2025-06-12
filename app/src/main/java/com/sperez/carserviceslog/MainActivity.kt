package com.sperez.carserviceslog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sperez.carserviceslog.ui.theme.CarServicesLogTheme
import com.sperez.carserviceslog.view.CreateNewUser
import com.sperez.carserviceslog.view.DisplayLogs
import com.sperez.carserviceslog.view.ForgotPassword
import com.sperez.carserviceslog.view.Loading
import com.sperez.carserviceslog.view.ServicesLogScreen
import com.sperez.carserviceslog.view.SignIn
import com.sperez.carserviceslog.viewModel.LogInViewModel
import com.sperez.carserviceslog.viewModel.ServicesLogViewModel

class MainActivity : ComponentActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth

        val viewModelLogIn: LogInViewModel = viewModels<LogInViewModel>().value
        val viewModelLogs: ServicesLogViewModel = viewModels<ServicesLogViewModel>().value

        setContent {

            val snackBarHostState = remember { SnackbarHostState() }
            val errorMessageId = viewModelLogIn.currentState.value.errorMessage
            val errorMessage = errorMessageId?.let {
                stringResource(it)
            }

            val successMessageId = viewModelLogIn.currentState.value.successMessage
            val successMessage = successMessageId?.let {
                stringResource(it)
            }

            val navController = rememberNavController()

            viewModelLogIn.onCreate(navController)

            LaunchedEffect(errorMessageId) {
                if (errorMessageId != null) {
                    snackBarHostState.showSnackbar(
                        message = errorMessage ?: "",
                        duration = SnackbarDuration.Short,
                    )
                    viewModelLogIn.clearMessages()
                }
            }
            LaunchedEffect(successMessageId) {
                if (successMessageId != null) {
                    snackBarHostState.showSnackbar(
                        message = successMessage ?: "",
                        duration = SnackbarDuration.Short,
                    )
                    viewModelLogIn.clearMessages()
                }
            }

            CarServicesLogTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    if (viewModelLogIn.currentState.value.isLoading) {
                        Loading()
                    }

                    NavigationStack(
                        Modifier.padding(innerPadding),
                        viewModelLogIn.isUserLogged(),
                        viewModelLogIn::dispatchEvent,
                        navController
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationStack(
    modifier: Modifier = Modifier,
    isUserLogged: Boolean,
    dispatchEvent: (CarServicesLogEvent) -> Unit,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination =
        if (isUserLogged) {
            Screen.ServicesLog.route
        }else {
            Screen.Login.route
        }
    ) {
        composable(route = Screen.Login.route) {
            SignIn(navController = navController, dispatchEvent = dispatchEvent, modifier = Modifier)
        }
        composable(
            route = Screen.ForgotPassword.route,
        ) {
            ForgotPassword(dispatchEvent = dispatchEvent)
        }
        composable(
            route = Screen.NewUser.route
        ) {
            CreateNewUser(dispatchEvent = dispatchEvent)
        }
        composable(route = Screen.ServicesLog.route) {
            DisplayLogs(modifier, dispatchEvent)
        }
        composable(route = Screen.ServiceDetail.route) {

        }
    }
}


