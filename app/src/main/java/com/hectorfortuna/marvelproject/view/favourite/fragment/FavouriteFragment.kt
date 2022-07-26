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
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.databinding.FragmentFavouriteBinding
import com.hectorfortuna.marvelproject.util.ConfirmDialog
import com.hectorfortuna.marvelproject.view.adapter.CharacterAdapter
import com.hectorfortuna.marvelproject.view.favourite.viewmodel.FavouriteViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class FavouriteFragment : Fragment() {
    lateinit var viewModel: FavouriteViewModel
    lateinit var repository: DatabaseRepository
    private lateinit var characterAdapter: CharacterAdapter
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    private lateinit var binding: FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = DatabaseRepositoryImpl(dao)
        viewModel = FavouriteViewModel(repository, Dispatchers.IO)
        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters().observe(viewLifecycleOwner) { results ->
            when {
                results.isNotEmpty() -> {
                    Timber.tag("LISTARESULT").i(results.toString())
                    setRecyclerView(results)
                }
                else -> {
                    setRecyclerView(results)
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

    private fun goToDetail(results: Results) {
        findNavController().navigate(
            R.id.action_favouriteFragment_to_detailFragment,
            Bundle().apply {
                putSerializable("CHARACTER", results)
            })
    }

    private fun deleteCharacters(results: Results) {
        ConfirmDialog(
            title = getString(R.string.confirmation),
            message = getString(R.string.delete_character_question),
            textYes = getString(R.string.delete_button),
            textNo = getString(R.string.no_button)
        ).apply {
            setListener {
                viewModel.deleteCharacters(results)
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