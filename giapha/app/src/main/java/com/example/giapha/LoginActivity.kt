package com.example.giapha

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.giapha.data.AuthResult
import com.example.giapha.databinding.ActivityLoginBinding
import com.example.giapha.ui.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge()
        setupInputListeners()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootScrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupInputListeners() {
        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.error = null
        }

        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.error = null
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()
            viewModel.login(email, password)
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Tính năng Quên mật khẩu đang phát triển", Toast.LENGTH_SHORT).show()
        }

        binding.tvRegisterNow.setOnClickListener {
            Toast.makeText(this, "Chuyển đến màn hình Đăng ký tài khoản", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.emailError.observe(this) { errorRes ->
            binding.tilEmail.error = errorRes?.let { getString(it) }
        }

        viewModel.passwordError.observe(this) { errorRes ->
            binding.tilPassword.error = errorRes?.let { getString(it) }
        }

        viewModel.authState.observe(this) { result ->
            when (result) {
                is AuthResult.Idle -> showLoading(false)
                is AuthResult.Loading -> showLoading(true)
                is AuthResult.Success -> {
                    showLoading(false)
                    Toast.makeText(this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show()
                    
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("EXTRA_USER_EMAIL", result.data.email)
                        putExtra("EXTRA_USER_NAME", result.data.name)
                    }
                    startActivity(intent)
                    finish()
                }
                is AuthResult.Error -> {
                    showLoading(false)
                    val errorMsg = result.rawMessage ?: getString(result.messageResId)
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.text = ""
            binding.btnLogin.isEnabled = false
            binding.etEmail.isEnabled = false
            binding.etPassword.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.text = getString(R.string.btn_login)
            binding.btnLogin.isEnabled = true
            binding.etEmail.isEnabled = true
            binding.etPassword.isEnabled = true
        }
    }
}
