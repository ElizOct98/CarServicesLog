package com.sperez.carserviceslog

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthRepository: IAuthRepository {

    private val firebaseAuth = Firebase.auth

    override suspend fun signIn(user: String, password: String) =
        try {
            firebaseAuth.signInWithEmailAndPassword(user, password).await()
            AuthResult.SignInSuccess(firebaseAuth.currentUser!!)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error(R.string.invalid_credentials_error)
        } catch (e: Exception) {
            AuthResult.Error(R.string.log_in_message_error)
        }

    override suspend fun resetPassword(user: String): AuthResult =
        try {
            firebaseAuth.sendPasswordResetEmail(user).await()
            AuthResult.Completed
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Error(R.string.invalid_user)
        } catch (e: Exception) {
            AuthResult.Error(R.string.log_in_message_error)
        }

    override fun isUserLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun createUser(user: String, password: String): AuthResult =
        try {
            firebaseAuth.createUserWithEmailAndPassword(user, password).await()
            AuthResult.Completed
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResult.Error(R.string.email_already_in_use_error)
        } catch (e: Exception) {
            AuthResult.Error(R.string.log_in_message_error)
        }

    override fun signOut() =
        try {
            firebaseAuth.signOut()
            AuthResult.Completed
        } catch (e: Exception) {
            AuthResult.Error(R.string.log_in_message_error)
        }
}

interface IAuthRepository {
    suspend fun signIn(user: String, password: String): AuthResult

    fun signOut(): AuthResult

    suspend fun createUser(user: String, password: String): AuthResult

    suspend fun resetPassword(user: String): AuthResult

    fun isUserLogged(): Boolean

    fun getCurrentUser(): FirebaseUser?
}

sealed class AuthResult {
    data object Completed : AuthResult()
    data class SignInSuccess(val user: FirebaseUser) : AuthResult()
    data class Error(val message: Int) : AuthResult()
}