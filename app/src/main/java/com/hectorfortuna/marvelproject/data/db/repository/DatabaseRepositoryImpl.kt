package com.hectorfortuna.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.model.Results

class DatabaseRepositoryImpl(private val dao: CharacterDAO):DatabaseRepository {
    override suspend fun insertCharacter(result: Results) =
        dao.insertCharacter(result)

    override fun getAllCharacters(): LiveData<List<Results>> =
        dao.getAllCharacters()

    override suspend fun deleteCharacter(result: Results) =
        dao.deleteCharacter(result)

    override suspend fun getFavouriteCharacter(characterId: Long): Results? =
        dao.getFavouriteCharacter(characterId)

    override suspend fun getFavouriteCharacterByUser(characterId: Long, email: String): Results? =
        dao.getFavouriteCharactersByUser(characterId, email)


    override fun getAllCharactersByUser(email: String): LiveData<List<Results>> =
        dao.getAllCharactersByUser(email)

}