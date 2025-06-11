package com.sperez.carserviceslog

sealed class ServicesLogEvent {
    data object DisplayLogs : ServicesLogEvent()
    data object NavigateNewLog: ServicesLogEvent()
    data object NavigateLogDescription: ServicesLogEvent()
}

sealed class ServicesLogState{
    data object Loading : ServicesLogState()
    data object DisplayingLogs : ServicesLogState()
    data object Error : ServicesLogState()

}