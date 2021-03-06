package com.hectorfortuna.marvelproject.view.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.databinding.CharacterDetailBinding
import com.hectorfortuna.marvelproject.view.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    private var checkCharacter: Boolean = false
    private lateinit var user: User
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    private lateinit var binding: CharacterDetailBinding
    private lateinit var favorites: Favorites

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = CharacterDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favorites = arguments?.getParcelable<Favorites>("FAVORITE") as Favorites
        repository = DatabaseRepositoryImpl(dao)

        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }

        viewModel = DetailViewModel.DetailViewModelProviderFactory(repository, Dispatchers.IO)
            .create(DetailViewModel::class.java)

        viewModel.verifySavedCharacter(favorites.id, user.email)

        binding.run {
            setImage(imgDetails)
            setImage(imgDetailsPrincipal)

            txtDetails.text = favorites.name
            txtDescription.text = favorites.description
            setFavoriteCharacter()
        }
        observeVMEvents()
    }

    private fun CharacterDetailBinding.setFavoriteCharacter() {
        fabDetails.setOnClickListener {
            checkCharacter = if (checkCharacter) {
                viewModel.deleteCharacters(favorites)
                fabDetails.setImageResource(R.drawable.ic_fab)
                false
            } else {
                val copyFavorite = favorites.copy(email = user.email)
                viewModel.insertFavorite(copyFavorite)
                fabDetails.setImageResource(R.drawable.ic_full_favourite)
                true
            }
        }
    }

    private fun observeVMEvents(){
        viewModel.response.observe(viewLifecycleOwner){

            when (it.status){
                Status.SUCCESS ->{
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                    }
                }
                Status.ERROR ->{
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING->{}
            }
        }

        viewModel.verifyCharacter.observe(viewLifecycleOwner) {

            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { exist ->
                        if (exist) {
                            binding.fabDetails.setImageResource(R.drawable.ic_full_favourite)
                        }
                        checkCharacter = exist
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING ->{}
            }
        }
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${favorites.thumbnail.path}.${favorites.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }
}