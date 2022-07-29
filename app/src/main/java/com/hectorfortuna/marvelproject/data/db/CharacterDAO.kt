package com.hectorfortuna.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.data.model.User

@Dao
interface CharacterDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorites: Favorites)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createNewUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getValidUser(email: String, password: String): User?

    @Delete
    suspend fun deleteCharacter(result: Favorites)

    @Query("SELECT * FROM favorites_table WHERE id = :characterId")
    suspend fun getFavouriteCharacter(characterId: Long): Favorites?

    @Query("SELECT * FROM favorites_table WHERE email = :email")
    fun getAllCharactersByUser(email: String): LiveData<List<Favorites>>

    @Query("SELECT * FROM favorites_table WHERE id = :characterId AND email = :email")
    fun getFavouriteCharactersByUser(characterId: Long,email: String): Favorites?
    

}