package com.hectorfortuna.marvelproject.view.register.fragment.photo.viewmodel

import androidx.lifecycle.*
import com.hectorfortuna.marvelproject.core.State
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.repository.registerrepository.RegisterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoViewModel(
    private val repository: RegisterRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _user = MutableLiveData<State<Boolean>>()
    val user: LiveData<State<Boolean>> = _user

    fun insertNewUserOnDatabase(user: User) {
        viewModelScope.launch {
            try {
                _user.value = State.loading(true)
                withContext(ioDispatcher) {
                    repository.registerNewUser(user)
                }
                _user.value = State.loading(false)
                _user.value = State.success(true)
            } catch (e: Exception) {
                _user.value = State.error(e)
            }
        }
    }

    class PhotoViewModelProvider(
        private val repository: RegisterRepository,
        private val ioDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
                return PhotoViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}