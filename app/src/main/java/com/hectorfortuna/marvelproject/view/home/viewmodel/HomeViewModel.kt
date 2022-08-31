package com.hectorfortuna.marvelproject.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectorfortuna.marvelproject.core.State
import com.hectorfortuna.marvelproject.data.model.CharacterResponse
import com.hectorfortuna.marvelproject.data.repository.character.CharacterRepository
import com.hectorfortuna.marvelproject.di.qualifier.Io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository,
    @Io val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _response = MutableLiveData<State<CharacterResponse>>()
    val response: LiveData<State<CharacterResponse>> = _response

    private val _search = MutableLiveData<State<CharacterResponse>>()
    val search: LiveData<State<CharacterResponse>>
        get() = _search

    fun getCharacters(apikey: String, hash: String, ts: Long,limit:Int, offset: Int = 0) {
        viewModelScope.launch {
            try {
                _response.value = State.loading(true)
                val response = withContext(ioDispatcher) {
                    repository.getCharacters(apikey, hash, ts,limit, offset )
                }
                _response.value = State.loading(false)
                _response.value = State.success(response)
            } catch (throwable: Throwable) {
                _response.value = State.loading(false)
                _response.value = State.error(throwable)
            }
        }
    }

    fun searchCharacters(nameStartsWith: String, apikey: String, hash: String, ts: Long) {
        viewModelScope.launch {
            try {
                _search.value = State.loading(true)
                val searchResponse = withContext(ioDispatcher){
                    repository.searchCharacters(nameStartsWith, apikey, hash, ts)
                }
                _search.value = State.loading(false)
                _search.value = State.success(searchResponse)
            } catch (throwable: Throwable){
                _search.value = State.loading(false)
                _search.value = State.error(throwable)
            }
        }

    }
}