package com.example.voidchallengejetpack.di

import com.example.voidchallengejetpack.data.remote.ShowsAPI
import com.example.voidchallengejetpack.repository.ShowRepository
import com.example.voidchallengejetpack.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShowRepository(
        api: ShowsAPI
    ) = ShowRepository(api)

    @Singleton
    @Provides
    fun provideShowAPI(): ShowsAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ShowsAPI::class.java)
    }

}