package com.sperez.carserviceslog.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.R
import com.sperez.carserviceslog.model.ServicesLog

@Composable
fun NewServiceLogForm(modifier: Modifier = Modifier, dispatchEvent: (CarServicesLogEvent) -> Unit) {


    var carName by remember {
        mutableStateOf("")
    }
    var kilometers by remember {
        mutableStateOf("")
    }
    var date by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }

    Column (modifier = modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(R.string.add_new_service))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = carName,
            onValueChange = { carName = it },
            label = { Text(stringResource(R.string.car_name)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = kilometers,
            onValueChange = { kilometers = it },
            label = { Text(stringResource(R.string.kilometers)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = date,
            onValueChange = { date = it },
            label = { Text(stringResource(R.string.date)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = price,
            onValueChange = { price = it },
            label = { Text(stringResource(R.string.price)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) }
        )


        Button(onClick = {
            val newService = ServicesLog(
                carName = carName,
                kilometers = kilometers,
                date = date,
                price = price,
                description = description
            )
            dispatchEvent(CarServicesLogEvent.NewServiceLog(newService))
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text(stringResource(R.string.save_service))
        }
    }
}


