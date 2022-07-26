package com.hectorfortuna.marvelproject.view.login.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.repository.loginrepository.LoginRepository
import com.hectorfortuna.marvelproject.data.repository.loginrepository.LoginRepositoryImpl
import com.hectorfortuna.marvelproject.databinding.ActivityLoginBinding
import com.hectorfortuna.marvelproject.util.Watcher
import com.hectorfortuna.marvelproject.util.setError
import com.hectorfortuna.marvelproject.util.toast
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

        repository = LoginRepositoryImpl(dao)
        viewModel = LoginViewModel.LoginViewModelProviderFactory(repository)
            .create(LoginViewModel::class.java)

        observeVMEvents()

        binding.run {
            loginButton.setOnClickListener {
                val email = binding.loginUserEdit.text.toString()
                val password = binding.loginPasswordEdit.text.toString()

                viewModel.login(email, password)
            }
            loginUserEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
    }

    private val watcher = Watcher{
        binding.loginButton.isEnabled = binding.loginUserEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun observeVMEvents(){
        viewModel.loginFieldErrorResId.observe(this){
            binding.loginUsernameLayout.setError(this@LoginActivity, it)
        }
        viewModel.passwordFieldErrorResId.observe(this){
            binding.loginPasswordLayout.setError(this@LoginActivity, it)
        }
        viewModel.loading.observe(this){
            binding.loginButton.progress(it)
        }
        viewModel.user.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        toast("Login com Sucesso ${user.name}")
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Login").i(it.error)
                }
                Status.LOADING ->{}
            }
        }
    }
}