package com.hectorfortuna.marvelproject.view.register.fragment.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.databinding.FragmentPasswordBinding
import com.hectorfortuna.marvelproject.util.setError
import com.hectorfortuna.marvelproject.view.register.fragment.password.viewmodel.PasswordViewModel

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PasswordViewModel.PasswordViewModelProviderFactory()
            .create(PasswordViewModel::class.java)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        observeVMEvents()
        goToPhotoStep()
    }

    private fun goToPhotoStep() {
        binding.nextButtonPassword.setOnClickListener {
            val password = binding.passwordEdit.text.toString()
            val passwordConfirmation = binding.passwordConfirmEdit.text.toString()

            if (viewModel.checkIfPasswordAreValid(password, passwordConfirmation) ) {
                navigateToNextStepWithValidPassword(password)
            }
        }
    }

    private fun navigateToNextStepWithValidPassword(password: String) {
        val userWithNewValues = User(user.email, user.name, password, null)

        findNavController().navigate(R.id.action_passwordFragment_to_photoFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", userWithNewValues)
            })
    }

    private fun observeVMEvents(){
        viewModel.passwordFieldErrorResId.observe(viewLifecycleOwner){
            binding.passwordLayout.setError(requireContext(), it)
        }
        viewModel.passwordIsDifferentFieldErrorResId.observe(viewLifecycleOwner){
            binding.confirmPasswordLayout.setError(requireContext(),it)
        }
    }
}