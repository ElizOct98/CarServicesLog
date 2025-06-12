package com.sperez.carserviceslog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.R
import com.sperez.carserviceslog.model.ServicesLog

@Composable
fun DisplayLogs(
    modifier: Modifier = Modifier,
    logs: State<List<ServicesLog>>
) {
    val services = logs.value

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(services.size) {
            val service = logs.value[it]
            Card(
                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ){
                Column {
                    Text(text = service.carName, modifier=Modifier.align(alignment = Alignment.CenterHorizontally).padding(top = 10.dp))
                    Column(
                        modifier=Modifier.padding(10.dp)
                    ) {
                        Text(text = service.kilometers + " km")
                        Text(text = service.date)
                        Text(text = "$ " + service.price)
                    }
                    Text(text = service.description , modifier=Modifier.padding(10.dp))

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayLogsTopBar(dispatchEvent: (CarServicesLogEvent) -> Unit) {
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
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.log_out)
                )
            }
        }
    )
}

@Composable
fun DisplayLogsFAB(dispatchEvent: (CarServicesLogEvent) -> Unit) {
    FloatingActionButton(
        onClick = { dispatchEvent(CarServicesLogEvent.NavigateNewLog) }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = ""
        )
    }
}



