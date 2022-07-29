package com.hectorfortuna.marvelproject.view.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectorfortuna.marvelproject.core.State
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
):ViewModel() {
    private val _delete = MutableLiveData<State<Boolean>>()
    val delete : LiveData<State<Boolean>>
    get() = _delete

    fun getCharacters(email: String) = repository.getAllCharactersByUser(email)

    fun deleteCharacters(favorites: Favorites) = viewModelScope.launch {
        try {
            _delete.value = State.loading(true)
            withContext(ioDispatcher) {
                repository.deleteCharacter(favorites)
            }
            _delete.value = State.loading(false)
            _delete.value = State.success(true)
        } catch (e: Exception){
            _delete.value = State.loading(false)
            _delete.value = State.error(e)
        }
    }
}