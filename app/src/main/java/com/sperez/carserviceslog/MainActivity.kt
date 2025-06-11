package com.sperez.carserviceslog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.sperez.carserviceslog.ui.theme.CarServicesLogTheme
import com.sperez.carserviceslog.view.LogInScreen
import com.sperez.carserviceslog.viewModel.LogInViewModel
import com.sperez.carserviceslog.viewModel.ServicesLogViewModel

class MainActivity : ComponentActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        val viewModelLogIn: LogInViewModel = viewModels<LogInViewModel>().value
        val viewModelLogs: LogInViewModel = viewModels<ServicesLogViewModel>().value
        setContent {
            CarServicesLogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   LogInScreen(Modifier.padding(innerPadding), viewModel.currentState, viewModel::dispatchEvent)
                }
            }
        }
    }
}


