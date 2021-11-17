package com.spitzer.igdbgames.di

import android.app.Application
import androidx.room.Room
import com.spitzer.igdbgames.core.network.ApiClient
import com.spitzer.igdbgames.repository.GameDetailsRepository
import com.spitzer.igdbgames.repository.GamesDetailsRepositoryImpl
import com.spitzer.igdbgames.repository.GamesRepository
import com.spitzer.igdbgames.repository.GamesRepositoryImpl
import com.spitzer.igdbgames.repository.retrofit.GamesService
import com.spitzer.igdbgames.repository.room.GameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGameRepository(
        service: GamesService,
        database: GameDatabase
    ): GamesRepository =
        GamesRepositoryImpl(service, database)

    @Provides
    @Singleton
    fun provideGameDetailsRepository(
        database: GameDatabase
    ): GameDetailsRepository =
        GamesDetailsRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient =
        ApiClient()

    @Provides
    @Singleton
    fun provideGamesService(apiClient: ApiClient): GamesService =
        apiClient.createService(GamesService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): GameDatabase =
        Room.databaseBuilder(app, GameDatabase::class.java, "Games.db")
            .build()

}
