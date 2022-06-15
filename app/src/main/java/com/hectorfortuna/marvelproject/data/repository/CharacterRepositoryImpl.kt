package com.hectorfortuna.marvelproject.data.repository

import com.hectorfortuna.marvelproject.data.model.CharacterResponse
import com.hectorfortuna.marvelproject.data.network.Service

class CharacterRepositoryImpl(private val api: Service): CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse =
        api.getCharacters(apikey,hash,ts)

}