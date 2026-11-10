package com.example.giapha.data

sealed class AuthResult<out T> {
    object Idle : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val messageResId: Int, val rawMessage: String? = null) : AuthResult<Nothing>()
}
