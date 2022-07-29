package com.hectorfortuna.marvelproject.view.login.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.repository.loginrepository.LoginRepository
import com.hectorfortuna.marvelproject.data.repository.loginrepository.LoginRepositoryMock
import com.hectorfortuna.marvelproject.databinding.ActivityLoginBinding
import com.hectorfortuna.marvelproject.util.Watcher
import com.hectorfortuna.marvelproject.util.setError
import com.hectorfortuna.marvelproject.util.toast
import com.hectorfortuna.marvelproject.view.home.activity.HomeActivity
import com.hectorfortuna.marvelproject.view.login.viewmodel.LoginViewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: LoginRepository
    private val dao: CharacterDAO by lazy { AppDatabase.getDb(applicationContext).characterDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        repository = LoginRepositoryMock()
        viewModel = LoginViewModel.LoginViewModelProviderFactory(repository)
            .create(LoginViewModel::class.java)

        observeVMEvents()

        binding.run {
            loginButton.setOnClickListener {
                val email = binding.loginUserEdit.text.toString()
                val password = binding.loginPasswordEdit.text.toString()

                viewModel.login(email, password)
            }
            loginUserEdit.setText("hectorsuarez@gmail.com")
            loginPasswordEdit.setText("12345678")
            loginUserEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
    }

    private val watcher = Watcher {
        binding.loginButton.isEnabled = binding.loginUserEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun observeVMEvents() {
        viewModel.loginFieldErrorResId.observe(this) {
            binding.loginUsernameLayout.setError(this@LoginActivity, it)
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
