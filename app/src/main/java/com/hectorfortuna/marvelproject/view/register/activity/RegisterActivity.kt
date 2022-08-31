package com.hectorfortuna.marvelproject.view.register.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.databinding.ActivityRegisterBinding
import com.hectorfortuna.marvelproject.view.login.activity.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_register_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
    fun goTo(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}