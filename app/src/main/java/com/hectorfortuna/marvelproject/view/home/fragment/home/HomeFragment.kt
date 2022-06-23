package com.hectorfortuna.marvelproject.view.home.fragment.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.BaseFragment
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.core.hasInternet
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.data.network.ApiService
import com.hectorfortuna.marvelproject.data.repository.CharacterRepository
import com.hectorfortuna.marvelproject.data.repository.CharacterRepositoryImpl
import com.hectorfortuna.marvelproject.databinding.FragmentHomeBinding
import com.hectorfortuna.marvelproject.util.apiKey
import com.hectorfortuna.marvelproject.util.hash
import com.hectorfortuna.marvelproject.util.ts
import com.hectorfortuna.marvelproject.view.adapter.CharacterAdapter
import com.hectorfortuna.marvelproject.view.home.fragment.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: CharacterRepository
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("CONNECTION").i(hasInternet(context).toString())

        repository = CharacterRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(repository, Dispatchers.IO)
            .create(HomeViewModel::class.java)

            checkConnection()
            observeVMEvents()
    }

    override fun checkConnection() {
        if(hasInternet(context)) {
            getCharacters()
        }else {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.connection_error))
                .setMessage(getString(R.string.verify_ethernet))
                .setPositiveButton(getString(R.string.confirm),{ _, _ ->})
                .show()

        }
    }

    private fun getCharacters() {
        val ts = ts()
        viewModel.getCharacters(apiKey(), hash(), ts.toLong())
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Sucesso").i(response.toString())
                        setRecyclerView(response.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Erro").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList) { character ->
            Timber.tag("Click").i(character.name)
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment2,
                Bundle().apply {
                    putSerializable("CHARACTER", character)
                })
        }
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList)
        binding.rvHomeFragment.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }
}

