package com.hectorfortuna.marvelproject.data.repository.loginrepository

import com.hectorfortuna.marvelproject.data.mockdatabase.MockDatabase
import com.hectorfortuna.marvelproject.data.model.User

class LoginRepositoryMock: LoginRepository {
    override suspend fun login(email: String, password: String): User? =
        MockDatabase.mockLogin(email,password)
}