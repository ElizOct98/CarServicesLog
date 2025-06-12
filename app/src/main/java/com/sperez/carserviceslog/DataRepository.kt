package com.sperez.carserviceslog

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sperez.carserviceslog.model.ServicesLog
import kotlinx.coroutines.tasks.await

class DataRepository: IDataRepository {

    val fireStore = Firebase.firestore
    override suspend fun addNewLog(
        userId: String,
        log: ServicesLog
    ): DataResult = try {
        val userCollection = fireStore.collection(userId)
        userCollection.add(log).await()
        DataResult.Completed
    } catch (e: Exception) {
        Log.e("Error", e.message ?: "")
        DataResult.Error(R.string.log_in_message_error)
    }

    override suspend fun getUserLogs(userId: String): DataResult =
        try {
            val logs = mutableListOf<ServicesLog>()
            val snapshot = fireStore.collection(userId).get().await()
            snapshot.documents.forEach {
                val carService = it.toObject(ServicesLog::class.java)
                if (carService != null) {
                    logs.add(carService)
                }
            }

            DataResult.GetDataSuccess(logs)
        } catch (e: Exception) {
            Log.e("Error", e.message ?: "")
            DataResult.Error(R.string.log_in_message_error)
        }
}

interface IDataRepository {
    suspend fun addNewLog(userId: String, log: ServicesLog): DataResult

    suspend fun getUserLogs(userId: String): DataResult
}

sealed class DataResult {
    data object Completed: DataResult()
    data class GetDataSuccess(val logs: List<ServicesLog>): DataResult()
    data class Error(val message: Int): DataResult()
}