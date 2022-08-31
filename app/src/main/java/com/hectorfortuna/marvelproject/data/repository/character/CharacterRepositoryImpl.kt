package com.hectorfortuna.marvelproject.data.repository.character

import com.hectorfortuna.marvelproject.data.model.CharacterResponse
import com.hectorfortuna.marvelproject.data.network.Service
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val api: Service) : CharacterRepository {
    override suspend fun getCharacters(
        apikey: String,
        hash: String,
        ts: Long,
        limit: Int,
        offset: Int
    ): CharacterResponse =
        api.getCharacters(apikey = apikey, hash = hash, ts = ts, limit = 50, offset = offset)

    override suspend fun searchCharacters(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse =
        api.searchCharacters(nameStartsWith = nameStartsWith, apikey = apikey, hash = hash, ts = ts)


}