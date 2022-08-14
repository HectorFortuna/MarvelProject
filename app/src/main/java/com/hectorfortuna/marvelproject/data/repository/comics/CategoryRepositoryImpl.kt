package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse
import com.hectorfortuna.marvelproject.data.network.Service

class CategoryRepositoryImpl(private val api: Service) : CategoryRepository {
    override suspend fun getCategory(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long,
        category: String
    ): ComicsResponse =
        api.getComics(apikey = apikey, hash = hash, ts = ts, id = id, category = category)

}