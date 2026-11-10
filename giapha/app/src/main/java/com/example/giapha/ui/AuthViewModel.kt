package com.example.giapha.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giapha.R
import com.example.giapha.data.AuthResult
import com.example.giapha.data.AuthValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class User(val email: String, val name: String)

class AuthViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthResult<User>>(AuthResult.Idle)
    val authState: LiveData<AuthResult<User>> = _authState

    private val _emailError = MutableLiveData<Int?>()
    val emailError: LiveData<Int?> = _emailError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?> = _passwordError

    fun login(email: String, password: String) {
        val emailErr = AuthValidator.validateEmail(email)
        val passwordErr = AuthValidator.validatePassword(password)

        _emailError.value = emailErr
        _passwordError.value = passwordErr

        if (emailErr != null || passwordErr != null) {
            return
        }

        _authState.value = AuthResult.Loading

        viewModelScope.launch {
            // Giả lập xử lý xác thực tài khoản qua API / Firebase
            delay(1500)

            // Giả lập tài khoản hợp lệ (Ví dụ: admin@example.com / 123456 hoặc bất kỳ email/pass hợp lệ)
            if (password == "123456" || password.length >= 6) {
                val user = User(email = email.trim(), name = email.substringBefore("@"))
                _authState.value = AuthResult.Success(user)
            } else {
                _authState.value = AuthResult.Error(R.string.auth_failed)
            }
        }
    }

    fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
        _authState.value = AuthResult.Idle
    }
}
