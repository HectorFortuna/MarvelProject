package com.hectorfortuna.marvelproject.data.repository.comics

import com.hectorfortuna.marvelproject.data.model.comics.ComicsResponse

interface CategoryRepository {
    suspend fun getCategory(apikey: String, hash: String, ts: Long, id: Long, category: String): ComicsResponse
}