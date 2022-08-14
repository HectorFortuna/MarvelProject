package com.hectorfortuna.marvelproject.view.register.fragment.photo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.repository.register.RegisterRepository
import com.hectorfortuna.marvelproject.data.repository.register.RegisterRepositoryImpl
import com.hectorfortuna.marvelproject.databinding.FragmentPhotoBinding
import com.hectorfortuna.marvelproject.view.register.fragment.photo.viewmodel.PhotoViewModel
import kotlinx.coroutines.Dispatchers


class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var repository: RegisterRepository
    private lateinit var user: User
    private var uriImage: Uri? = null

    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    private var getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                setImageFromGallery(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        repository = RegisterRepositoryImpl(dao)
        viewModel = PhotoViewModel.PhotoViewModelProvider(repository, Dispatchers.IO)
            .create(PhotoViewModel::class.java)


        clickToChoosePhoto()
        insertUserOnDatabase()
        createUserWithoutPhoto()
        observeVMEvents()
    }

    private fun createUserWithoutPhoto() {
        binding.registerChooseLater.setOnClickListener {
            uriImage = Uri.parse("")
            val finalUser = user.copy(photo = uriImage)
            viewModel.insertNewUserOnDatabase(finalUser)
        }
    }

    private fun clickToChoosePhoto() {
        binding.registerCardview.setOnClickListener {
            gallery()
        }
    }

    private fun gallery() = getContent.launch("image/*")

    private fun setImageFromGallery(uri: Uri) {
        binding.registerImage.setImageURI(uri)
        uriImage = uri
    }

    private fun insertUserOnDatabase() {
        if (uriImage != null) {
            binding.nextButtonPhoto.setOnClickListener {
                val finalUser = user.copy(photo = uriImage)
                viewModel.insertNewUserOnDatabase(finalUser)
            }
        } else {
            uriImage = Uri.parse("")
            val finalUser = user.copy(photo = uriImage)
            viewModel.insertNewUserOnDatabase(finalUser)
        }
    }

    private fun makeUserWithOrWithoutPhoto(user: User) {

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
}