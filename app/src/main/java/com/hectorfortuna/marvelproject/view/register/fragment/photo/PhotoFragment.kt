package com.hectorfortuna.marvelproject.view.register.fragment.photo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.databinding.FragmentPhotoBinding
import com.hectorfortuna.marvelproject.view.register.fragment.photo.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private val viewModel by viewModels<PhotoViewModel>()
    private lateinit var user: User
    private var uriImage: Uri? = null

    private var getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri?.let {
            setImageFromGallery(it)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User


        clickToChoosePhoto()
        insertUserOnDatabase(user)
        observeVMEvents()
        backButton()
    }

    private fun clickToChoosePhoto() {
        binding.registerCardview.setOnClickListener {
            gallery()
        }
    }

    private fun gallery() = getContent.launch("image/*")

    private fun setImageFromGallery(uri: Uri){
        binding.registerImage.setImageURI(uri)
        uriImage = uri
    }

    private fun insertUserOnDatabase(user: User) {
        binding.nextButtonPhoto.setOnClickListener {
            if (uriImage != null){
                makeUserWithPhotoOrNot(user)
            } else {
                uriImage = Uri.parse("")
                makeUserWithPhotoOrNot(user)
            }
        }
    }

    private fun makeUserWithPhotoOrNot(user: User){
        val finalUser = user.copy(photo = uriImage)
        viewModel.insertNewUserOnDatabase(finalUser)
    }

    private fun observeVMEvents() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        goToNextStepWithValidUser()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao cadastrar usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun goToNextStepWithValidUser() {
        findNavController().navigate(R.id.action_photoFragment_to_welcomeFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", user)
            })
    }

    private fun backButton(){
        binding.backButtonPhoto.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}