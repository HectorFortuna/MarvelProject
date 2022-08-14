package com.hectorfortuna.marvelproject.data.repository.login

import com.hectorfortuna.marvelproject.data.model.User

interface LoginRepository {
    suspend fun login(email: String, password: String): User?
}