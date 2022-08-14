package com.hectorfortuna.marvelproject.data.repository.register

import com.hectorfortuna.marvelproject.data.model.User

interface RegisterRepository {
    suspend fun registerNewUser(user: User)
}