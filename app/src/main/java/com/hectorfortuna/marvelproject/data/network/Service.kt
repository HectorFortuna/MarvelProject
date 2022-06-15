package com.hectorfortuna.marvelproject.data.network

import com.hectorfortuna.marvelproject.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query
//"https://gateway.marvel.com"
//https://gateway.marvel.com/v1/public/characters?apikey=4c176e046daaad0a550fe0c820ae3430&hash=2502c1d02358094d41f0d24b6ea93e10&ts=1653066205
//"4c176e046daaad0a550fe0c820ae3430"

interface Service {
    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): CharacterResponse
}