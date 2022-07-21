package com.hectorfortuna.marvelproject.view.home.fragment.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.BaseFragment
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.core.hasInternet
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.data.network.ApiService
import com.hectorfortuna.marvelproject.data.repository.CharacterRepository
import com.hectorfortuna.marvelproject.data.repository.CharacterRepositoryImpl
import com.hectorfortuna.marvelproject.databinding.FragmentHomeBinding
import com.hectorfortuna.marvelproject.util.ConfirmDialog
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
    private var offsetCharacters: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()
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
        paginationSetup()
    }

    private fun search(menu: Menu) {
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                when (newText) {
                    "" -> getCharacters()
                    else -> searchCharacter(newText.toString())
                }
                return false
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu)
        search(menu)
    }


    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.includeToolbar.toolbarLayout)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun checkConnection() {
        if (hasInternet(context)) {
            getCharacters()
        } else {
            ConfirmDialog(
                title = getString(R.string.internet_connection_error),
                message = getString(R.string.try_again_message),
                textYes = getString(R.string.try_again_button),
                textNo = getString(R.string.cancel_button)
            ).apply {
                setListener {
                    checkConnection()
                }
            }.show(parentFragmentManager, "Connection")


        }
    }

    private fun getCharacters(limit: Int = 50, offset: Int = 0) {
        val ts = ts()
        viewModel.getCharacters(apiKey(), hash(), ts.toLong(), limit, offset)
    }

    private fun searchCharacter(nameStart: String) {
        val ts = ts()
        viewModel.searchCharacters(nameStart, apiKey(), hash(), ts.toLong())
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
                    val snack = Snackbar.make(binding.root, "Not found", Snackbar.LENGTH_INDEFINITE)
                    snack.setAction("Confirmar") {}
                    snack.show()
                }
                Status.LOADING -> {}
            }
        }

        viewModel.search.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { searchResponse ->
                        Timber.tag("Sucesso").i(searchResponse.toString())
                        setRecyclerView(searchResponse.data.results)
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
        characterAdapter = CharacterAdapter(characterList, { character ->
            Timber.tag("Click").i(character.name)
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment2,
                Bundle().apply {
                    putSerializable("CHARACTER", character)
                }
            )
        })
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList)
        binding.rvHomeFragment.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }
    private fun paginationSetup() {
        binding.fabNext.setOnClickListener {
            if (offsetCharacters >= 0) {
                offsetCharacters += 50
                getCharacters(offset = offsetCharacters)
            }
        }
        binding.fabPrevious.setOnClickListener {
            if (offsetCharacters >= 50) {
                offsetCharacters -= 50
                getCharacters(offset = offsetCharacters)
            }
        }
    }

}

