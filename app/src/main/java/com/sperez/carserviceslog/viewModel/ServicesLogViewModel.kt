package com.sperez.carserviceslog.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.sperez.carserviceslog.CarServicesLogEvent
import com.sperez.carserviceslog.CarServicesLogState
import com.sperez.carserviceslog.ServicesLogState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ServicesLogViewModel: ViewModel() {


    private var _currentStateLogIn = mutableStateOf(CarServicesLogState.Logged)
    private var _currentStateServicesLog = mutableStateOf(ServicesLogState.Loading)

    val currentStateLogIn : State<CarServicesLogState> = _currentStateLogIn
    val currentStateServicesLog : State<ServicesLogState> =  _currentStateServicesLog

    private var _events = MutableSharedFlow<ServicesLogState>()


    init {
        viewModelScope.launch {
            _events.collect{
                when(it){
                    ServicesLogState.DisplayingLogs -> TODO()
                    ServicesLogState.Error -> TODO()
                    ServicesLogState.Loading -> TODO()
                }
            }
        }
    }

    fun dispatchEvent(event: ServicesLogState) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}