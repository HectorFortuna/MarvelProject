package com.hectorfortuna.marvelproject.data.repository

import com.hectorfortuna.marvelproject.data.model.CharacterResponse
import com.hectorfortuna.marvelproject.data.network.Service

class CharacterRepositoryImpl(private val api: Service) : CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long, limit:Int, offset:Int): CharacterResponse =
        api.getCharacters(apiKey = apikey, hash = hash, ts = ts, limit = 50, offset = offset)

    override suspend fun searchCharacters(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse =
        api.searchCharacters(nameStartsWith = nameStartsWith, apiKey = apikey, hash = hash, ts = ts)

}