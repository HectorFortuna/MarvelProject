package com.hectorfortuna.marvelproject.view.home.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.hectorfortuna.marvelproject.core.Status
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

class HomeFragment : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var repository: CharacterRepository
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        repository = CharacterRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(repository, Dispatchers.IO)
            .create(HomeViewModel::class.java)

        getCharacters()
        observeVMEvents()

    }

    private fun getCharacters() {
        val ts = ts()
        viewModel.getCharacters(apiKey(), hash(), ts.toLong())
    }

    private fun observeVMEvents(){
        viewModel.response.observe(viewLifecycleOwner){
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe

            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let {response ->
                        Timber.tag("Sucesso").i(response.toString())
                        setRecyclerView(response.data.results as MutableList<Results>)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Erro").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }


    private fun setAdapter(characterList: MutableList<Results>){
        characterAdapter = CharacterAdapter(characterList)
}
    private fun setRecyclerView(characterList: MutableList<Results>){
        setAdapter(characterList)
        binding.rvHomeFragment.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
        //characterAdapter = CharacterAdapter(characterList)
        //binding.rvHomeFragment.apply {
        //    setHasFixedSize(true)
        //    adapter = characterAdapter
    }
}