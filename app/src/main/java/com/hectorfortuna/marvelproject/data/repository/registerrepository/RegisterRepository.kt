package com.hectorfortuna.marvelproject.data.repository.registerrepository

import com.hectorfortuna.marvelproject.data.model.User

interface RegisterRepository {
    suspend fun registerNewUser(user: User)
}