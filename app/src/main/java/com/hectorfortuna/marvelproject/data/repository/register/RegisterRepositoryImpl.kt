package com.hectorfortuna.marvelproject.data.repository.register

import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.User
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val dao: CharacterDAO): RegisterRepository {
    override suspend fun registerNewUser(user: User) {
        dao.createNewUser(user)
    }
}