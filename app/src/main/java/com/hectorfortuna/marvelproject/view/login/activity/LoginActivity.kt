package com.hectorfortuna.marvelproject.view.login.activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.databinding.ActivityLoginBinding
import com.hectorfortuna.marvelproject.util.Watcher
import com.hectorfortuna.marvelproject.util.setError
import com.hectorfortuna.marvelproject.util.toast
import com.hectorfortuna.marvelproject.view.home.activity.HomeActivity
import com.hectorfortuna.marvelproject.view.login.viewmodel.LoginViewModel
import com.hectorfortuna.marvelproject.view.register.activity.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        observeVMEvents()

        binding.run {
            onClickLoginButton()
            goToRegisterActivity()
            loginUserEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)

        }
    }

    private fun ActivityLoginBinding.goToRegisterActivity() {
        loginRegisterButton.setOnClickListener {
            goTo(null, RegisterActivity::class.java)
        }
    }

    private fun ActivityLoginBinding.onClickLoginButton() {
        loginButton.setOnClickListener {
            val email = binding.loginUserEdit.text.toString()
            val password = binding.loginPasswordEdit.text.toString()
                viewModel.login(email, password)
            }
        }


    private val watcher = Watcher {
        binding.loginButton.isEnabled = binding.loginUserEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun observeVMEvents() {
        viewModel.loginFieldErrorResId.observe(this) {
            binding.loginUserLayout.setError(this@LoginActivity, it)
        }
        viewModel.passwordFieldErrorResId.observe(this) {
            binding.loginPasswordLayout.setError(this@LoginActivity, it)
        }
        viewModel.loading.observe(this) {
            binding.loginButton.progress(it)
        }
        viewModel.user.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        goTo(user, HomeActivity::class.java)
                    }
                }
                Status.ERROR -> {
                    toast("Erro ao logar")
                    Timber.tag("Login").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun <T> goTo(user: User?, clazz: Class<T>) {
        val intent = Intent(this@LoginActivity, clazz)
        user?.let {
            intent.putExtra("USER", user)
        }
        startActivity(intent)
        finish()
    }
}
