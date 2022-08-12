package com.hectorfortuna.marvelproject.view.favourite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.model.converterToResult
import com.hectorfortuna.marvelproject.databinding.FragmentFavouriteBinding
import com.hectorfortuna.marvelproject.util.ConfirmDialog
import com.hectorfortuna.marvelproject.view.adapter.CharacterAdapter
import com.hectorfortuna.marvelproject.view.favourite.viewmodel.FavouriteViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class FavouriteFragment : Fragment() {
    lateinit var viewModel: FavouriteViewModel
    lateinit var repository: DatabaseRepository
    private lateinit var user: User
    private lateinit var characterAdapter: CharacterAdapter
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    private lateinit var binding: FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = DatabaseRepositoryImpl(dao)

        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }

        viewModel = FavouriteViewModel(repository, Dispatchers.IO)
        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters(user.email).observe(viewLifecycleOwner) {
            when {
                it.isNotEmpty() -> {
                    Timber.tag("LISTARESULT").i(it.toString())
                    setRecyclerView(converterToResult(it))
                }
                else -> {
                    setRecyclerView(converterToResult(it))
                }
            }
        }

        viewModel.delete.observe(viewLifecycleOwner){ state->
            when(state.status){
                Status.SUCCESS ->{
                    state.data?.let {
                        Toast.makeText(requireContext(), "Personagem Deletado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Status.ERROR->{

                }
                Status.LOADING->{

                }
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList, ::goToDetail, ::deleteCharacters)
    }

    private fun goToDetail(favorites: Favorites) {
        findNavController().navigate(
            R.id.action_favouriteFragment_to_detailFragment,
            Bundle().apply {
                putParcelable("FAVORITE", favorites)
            })
    }

    private fun deleteCharacters(favorites: Favorites) {
        ConfirmDialog(
            title = getString(R.string.confirmation),
            message = getString(R.string.delete_character_question),
            textYes = getString(R.string.delete_button),
            textNo = getString(R.string.no_button)
        ).apply {
            setListener {
                val newFavourites = favorites.copy(email = user.email)
                viewModel.deleteCharacters(newFavourites)
            }
        }.show(parentFragmentManager, "Dialog")
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList)
        binding.rvFavourite.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }

}