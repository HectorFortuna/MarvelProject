package com.hectorfortuna.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.data.model.User

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(result: Results)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNewUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getValidUser(email: String, password: String): User?

    @Query("SELECT * FROM results_table")
    fun getAllCharacters(): LiveData<List<Results>>

    @Delete
    suspend fun deleteCharacter(result: Results)

    @Query("SELECT * FROM results_table WHERE id = :characterId")
    suspend fun getFavouriteCharacter(characterId: Long): Results?

    @Query("SELECT * FROM results_table WHERE email = :email")
    fun getAllCharactersByUser(email: String): LiveData<List<Results>>

    @Query("SELECT * FROM results_table WHERE id = :characterId AND email = :email")
    fun getFavouriteCharactersByUser(characterId: Long,email: String): Results?
    

}