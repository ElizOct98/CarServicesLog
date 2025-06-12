package com.sperez.carserviceslog.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sperez.carserviceslog.ServicesLogEvent
import com.sperez.carserviceslog.ServicesLogState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ServicesLogViewModel: ViewModel() {



    private var _currentStateServicesLog = mutableStateOf<ServicesLogState>(ServicesLogState.Loading)

    val currentStateServicesLog : State<ServicesLogState> =  _currentStateServicesLog

    private var _events = MutableSharedFlow<ServicesLogEvent>()
    val db = Firebase.firestore



    init {
        viewModelScope.launch {
            _events.collect{
                when(it){
                    ServicesLogEvent.DisplayLogs -> displayLogs()
                    ServicesLogEvent.NavigateLogDescription -> {}
                    ServicesLogEvent.NavigateNewLog -> {}
                }
            }
        }
    }

    fun dispatchEventServicesLog(event: ServicesLogEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    fun displayLogs(){
        _currentStateServicesLog.value = ServicesLogState.DisplayingLogs
    }


}