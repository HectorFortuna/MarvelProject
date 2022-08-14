package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse

interface CategoryRepository {
    suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse

    suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse
}