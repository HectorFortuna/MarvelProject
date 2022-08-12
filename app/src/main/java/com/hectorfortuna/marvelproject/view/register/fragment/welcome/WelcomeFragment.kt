package com.hectorfortuna.marvelproject.view.register.fragment.welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.databinding.FragmentWelcomeBinding
import com.hectorfortuna.marvelproject.view.login.activity.LoginActivity


class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
        goToHomePage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User
        goToHomePage()
        welcomeWith(user.name)

    }

    private fun goToHomePage() {
        binding.goToHomeButton.setOnClickListener {

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun welcomeWith(username: String){
        val welcomeName = "SEJA BEM VINDO(A), $username!"
        binding.welcome.text = welcomeName
    }


}