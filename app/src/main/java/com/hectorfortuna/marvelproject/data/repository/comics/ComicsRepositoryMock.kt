package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse

class ComicsRepositoryMock: CategoryRepository {
    override suspend fun getCategory(apikey: String, hash: String, ts: Long, id: Long, category: String): ComicsResponse {
        TODO()
    }
}