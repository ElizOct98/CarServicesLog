package com.sperez.carserviceslog

sealed class NewServiceLogEvent {
    data object DisplayForm : NewServiceLogEvent()
    data object NavigateServicesLog: NewServiceLogEvent()
}

sealed class NewServiceLogState{
    data object DisplayingFrom : NewServiceLogState()
    data object Successful : NewServiceLogState()
    data object Error : NewServiceLogState()

}