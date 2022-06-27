package com.hectorfortuna.marvelproject.view.home.fragment.favourite.viewmodel

import androidx.lifecycle.ViewModel
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineDispatcher

class FavouriteViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
):ViewModel() {

    fun getCharacters() = repository.getAllCharacters()
}