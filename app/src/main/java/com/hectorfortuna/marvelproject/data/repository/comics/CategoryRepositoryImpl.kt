package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse
import com.hectorfortuna.marvelproject.data.network.Service
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val api: Service) : CategoryRepository {
    override suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse =
        api.getComics(apikey = apikey, hash = hash, ts = ts, id = id)

    override suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse =
        api.getSeries(id = id, apikey = apikey, hash = hash, ts = ts)

}