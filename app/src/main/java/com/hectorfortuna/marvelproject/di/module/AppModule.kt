package com.hectorfortuna.marvelproject.di.module

import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.hectorfortuna.marvelproject.data.repository.character.CharacterRepository
import com.hectorfortuna.marvelproject.data.repository.character.CharacterRepositoryImpl
import com.hectorfortuna.marvelproject.data.repository.comics.CategoryRepository
import com.hectorfortuna.marvelproject.data.repository.comics.CategoryRepositoryImpl
import com.hectorfortuna.marvelproject.data.repository.login.LoginRepository
import com.hectorfortuna.marvelproject.data.repository.login.LoginRepositoryImpl
import com.hectorfortuna.marvelproject.data.repository.register.RegisterRepository
import com.hectorfortuna.marvelproject.data.repository.register.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepository: CategoryRepositoryImpl
    ): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindCharacterRepository(
        characterRepository: CharacterRepositoryImpl
    ): CharacterRepository

    @Singleton
    @Binds
    abstract fun bindDatabaseRepository(
        databaseRepository: DatabaseRepositoryImpl
    ): DatabaseRepository

    @Singleton
    @Binds
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Singleton
    @Binds
    abstract fun bindRegisterRepository(
        registerRepository: RegisterRepositoryImpl
    ): RegisterRepository
}