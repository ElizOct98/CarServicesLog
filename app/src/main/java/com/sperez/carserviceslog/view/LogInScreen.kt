package com.sperez.carserviceslog.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.CarServicesLogState
import com.sperez.carserviceslog.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    modifier: Modifier,
    state: State<CarServicesLogState>,
    dispatchEvent: (CarServicesLogEvent) -> Unit
) {
    val uiState = remember { state }
    when(uiState.value){
        is CarServicesLogState.Error -> {
            ModalBottomSheet(
                onDismissRequest = {}
            ) {
                Text(stringResource((uiState.value as CarServicesLogState.Error).message))
            }
        }
        CarServicesLogState.Loading -> Loading(modifier)
        CarServicesLogState.Logged -> ServicesLogScreen(modifier,)
        CarServicesLogState.NotLogged -> SignIn(modifier, dispatchEvent)
        is CarServicesLogState.ResetPasswordSuccessful -> SignIn(modifier,dispatchEvent)
        CarServicesLogState.CreateUser -> CreateNewUser(modifier, dispatchEvent)
        CarServicesLogState.ForgotPassword -> ForgotPassword (modifier, dispatchEvent)
    }
}
@Composable
fun Loading(modifier: Modifier = Modifier){
    Column(modifier=modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            modifier = Modifier.width(40.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(modifier = Modifier.padding(top=16.dp), text = stringResource(R.string.loading))
    }

}

@Composable
fun SignIn(modifier: Modifier = Modifier, dispatchEvent: (CarServicesLogEvent) -> Unit){
    var user by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Column (modifier = modifier.fillMaxSize().padding(horizontal = 32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "LogIn")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = user,
            onValueChange = { user = it },
            label = { Text(stringResource(R.string.email)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) }

        )
        Button(onClick = {
            dispatchEvent(CarServicesLogEvent.SignIn(user, password))
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
           Text(stringResource(R.string.log_in))
        }

        Text(stringResource(R.string.new_user), color = Color.Blue, textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { dispatchEvent(CarServicesLogEvent.NavigateNewUser ) })
        Text(stringResource(R.string.forgot_my_password), color = Color.Blue, textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { dispatchEvent(CarServicesLogEvent.NavigateForgotPassword) })
    }
}
@Composable
fun CreateNewUser(modifier: Modifier = Modifier, dispatchEvent: (CarServicesLogEvent) -> Unit) {
    var user by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }

    Column (modifier = modifier.fillMaxSize().padding(horizontal = 32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(R.string.log_in))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = user,
            onValueChange = { user = it },
            label = { Text(stringResource(R.string.email)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) }

        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(R.string.confirm_password)) }

        )
        Button(onClick = {
            dispatchEvent(CarServicesLogEvent.NewUser(user, password))
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(stringResource(R.string.register_user))
        }
    }
}

@Composable
fun ForgotPassword(modifier: Modifier = Modifier, dispatchEvent: (CarServicesLogEvent) -> Unit){
    Column (modifier=modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(R.string.forgot_your_password))
        Text(text = stringResource(R.string.enter_email_address))
        var user by remember {
            mutableStateOf("")
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = user,
            onValueChange = { user = it },
            label = { Text(stringResource(R.string.email)) }
        )
        Button(onClick = {
            dispatchEvent(CarServicesLogEvent.ForgotPassword(user))
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(stringResource(R.string.request_link))
        }
    }
}

@Composable
@Preview
fun Preview(){
    Loading()
}