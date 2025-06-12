package com.sperez.carserviceslog.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.R
import com.sperez.carserviceslog.ServicesLogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesLogScreen(
    modifier: Modifier,
    state: State<ServicesLogState>,
    dispatchEvent: (CarServicesLogEvent) -> Unit

) {
    val uiState = remember { state }
    Scaffold (
       modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            dispatchEvent(CarServicesLogEvent.SignOut)
                        }
                    ) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.log_out)
                        )
                    }
                }
            )
        },
        floatingActionButton ={
            FloatingActionButton(
                onClick = { dispatchEvent(CarServicesLogEvent.NavigateNewLog) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_new_log)
                )
            }
        }
    ){
        innerPadding ->
        when(uiState.value){
            ServicesLogState.DisplayingLogs -> DisplayLogs(modifier.padding(innerPadding),dispatchEvent)
            ServicesLogState.Error -> {
                ModalBottomSheet(
                    onDismissRequest = {}
                ) {
                    Text(stringResource((R.string.log_in_message_error)))
                }
            }
            ServicesLogState.Loading -> LoadingLogs(modifier.padding(innerPadding))
        }
    }
}


@Composable
fun LoadingLogs(modifier: Modifier = Modifier){
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
fun DisplayLogs(modifier: Modifier = Modifier,dispatchEvent: (CarServicesLogEvent) -> Unit){
    Column(modifier=modifier.fillMaxSize().clickable { dispatchEvent(CarServicesLogEvent.NavigateLogDescription ) }
        , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(top=16.dp), text = "Service")
        Text(modifier = Modifier.padding(top=16.dp), text = "Kilometers")
        Text(modifier = Modifier.padding(top=16.dp), text = "Date")
    }
}

