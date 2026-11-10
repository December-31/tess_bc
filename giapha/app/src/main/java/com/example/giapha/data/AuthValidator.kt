package com.example.giapha.data

import android.util.Patterns
import com.example.giapha.R

object AuthValidator {

    fun validateEmail(email: String): Int? {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> R.string.error_empty_email
            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> R.string.error_invalid_email
            else -> null
        }
    }

    fun validatePassword(password: String): Int? {
        return when {
            password.isEmpty() -> R.string.error_empty_password
            password.length < 6 -> R.string.error_short_password
            else -> null
        }
    }
}
