package com.hectorfortuna.marvelproject.data.repository.login

import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.User
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val dao: CharacterDAO) : LoginRepository{
    override suspend fun login(email: String, password: String): User? =
        dao.getValidUser(email, password)
    }
