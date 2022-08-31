package com.hectorfortuna.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.Favorites
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(private val dao: CharacterDAO) : DatabaseRepository {

    override suspend fun insertFavorite(favorites: Favorites) =
        dao.insertFavorite(favorites)


    override suspend fun deleteCharacter(result: Favorites) =
        dao.deleteCharacter(result)


    override suspend fun getFavouriteCharacterByUser(characterId: Long, email: String): Favorites? =
        dao.getFavouriteCharactersByUser(characterId, email)


    override fun getAllCharactersByUser(email: String): LiveData<List<Favorites>> =
        dao.getAllCharactersByUser(email)

}