package com.hectorfortuna.marvelproject.data.repository

import com.hectorfortuna.marvelproject.data.model.CharacterResponse

interface CharacterRepository {
    suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse
    suspend fun searchCharacters(nameStartsWith: String, apikey: String, hash: String, ts: Long):CharacterResponse
}