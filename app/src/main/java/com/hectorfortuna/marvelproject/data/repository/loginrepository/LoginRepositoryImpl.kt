package com.hectorfortuna.marvelproject.data.repository.loginrepository

import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.User

class LoginRepositoryImpl(private val dao: CharacterDAO) : LoginRepository{
    override suspend fun login(email: String, password: String): User? {
        TODO("Not yet implemented")
    }
}