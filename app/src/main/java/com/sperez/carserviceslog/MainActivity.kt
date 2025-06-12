package com.sperez.carserviceslog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sperez.carserviceslog.model.ServicesLog
import com.sperez.carserviceslog.ui.theme.CarServicesLogTheme
import com.sperez.carserviceslog.view.CreateNewUser
import com.sperez.carserviceslog.view.DisplayLogs
import com.sperez.carserviceslog.view.ForgotPassword
import com.sperez.carserviceslog.view.Loading
import com.sperez.carserviceslog.view.NewServiceLogForm
import com.sperez.carserviceslog.view.SignIn
import com.sperez.carserviceslog.viewModel.LogInViewModel

class MainActivity : ComponentActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth

        val viewModel: LogInViewModel = viewModels<LogInViewModel>().value

        setContent {

            val snackBarHostState = remember { SnackbarHostState() }
            val errorMessageId = viewModel.currentState.value.errorMessage
            val errorMessage = errorMessageId?.let {
                stringResource(it)
            }

            val successMessageId = viewModel.currentState.value.successMessage
            val successMessage = successMessageId?.let {
                stringResource(it)
            }

            val navController = rememberNavController()

            viewModel.onCreate(navController)

            LaunchedEffect(errorMessageId) {
                if (errorMessageId != null) {
                    snackBarHostState.showSnackbar(
                        message = errorMessage ?: "",
                        duration = SnackbarDuration.Short,
                    )
                    viewModel.clearMessages()
                }
            }
            LaunchedEffect(successMessageId) {
                if (successMessageId != null) {
                    snackBarHostState.showSnackbar(
                        message = successMessage ?: "",
                        duration = SnackbarDuration.Short,
                    )
                    viewModel.clearMessages()
                }
            }

            CarServicesLogTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    topBar = viewModel.currentState.value.topBar,
                    floatingActionButton = viewModel.currentState.value.floatingActionButton
                ) { innerPadding ->
                    if (viewModel.currentState.value.isLoading) {
                        Loading()
                    }

                    NavigationStack(
                        Modifier.padding(innerPadding),
                        viewModel.isUserLogged(),
                        viewModel::dispatchEvent,
                        navController,
                        viewModel.logs
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
    navController: NavHostController,
    logs: State<List<ServicesLog>>
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
            DisplayLogs(modifier, logs)
            dispatchEvent(CarServicesLogEvent.DisplayLogs)
        }
        composable(route = Screen.NewServiceLog.route) {
            dispatchEvent(CarServicesLogEvent.HideFAB)
            NewServiceLogForm(modifier, dispatchEvent)
        }
    }
}


