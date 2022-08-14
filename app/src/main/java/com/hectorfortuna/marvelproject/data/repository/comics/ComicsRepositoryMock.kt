package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse

class ComicsRepositoryMock: CategoryRepository {

    override suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse {
        TODO("Not yet implemented")
    }
}