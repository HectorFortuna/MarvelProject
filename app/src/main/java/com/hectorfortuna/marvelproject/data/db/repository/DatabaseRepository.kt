package com.hectorfortuna.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.hectorfortuna.marvelproject.data.model.Favorites

interface DatabaseRepository {

    suspend fun insertFavorite(favorites: Favorites)
    suspend fun deleteCharacter(result: Favorites)
    suspend fun getFavouriteCharacterByUser(characterId: Long, email: String):Favorites?
    fun getAllCharactersByUser(email: String): LiveData<List<Favorites>>
}