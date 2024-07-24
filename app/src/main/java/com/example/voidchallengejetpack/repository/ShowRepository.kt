package com.example.voidchallengejetpack.repository

import com.example.voidchallengejetpack.data.remote.ShowsAPI
import com.example.voidchallengejetpack.data.remote.responses.Keywords
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.data.remote.responses.ShowsResultList
import com.example.voidchallengejetpack.data.remote.responses.ShowDetails
import com.example.voidchallengejetpack.data.remote.responses.TVShowGenres
import com.example.voidchallengejetpack.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShowRepository @Inject constructor(
    private val api: ShowsAPI
) {
    suspend fun getPopularShows(): Resource<ShowsResultList> {
        val response = try {
            api.getPopularShows()
        } catch (e: Exception) {
            return Resource.Error("Check your internet connection...")
        }
        return Resource.Success(response)
    }

    suspend fun getShowGenres(): Resource<TVShowGenres> {
        val response = try {
            api.getGenres()
        } catch (e: Exception) {
            return Resource.Error("Failed to retrieve genres...")
        }
        return Resource.Success(response)
    }

    suspend fun searchShows(query: String): Resource<ShowsResultList> {
        val response = try {
            api.searchShows(query)
        } catch (e: Exception) {
            return Resource.Error("Searching failed... ")
        }
        return Resource.Success(response)
    }

    suspend fun getShowDetails(showId: Int): Resource<ShowDetails> {
        val response = try {
            api.getShowDetails(showId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getShowKeywords(showId: Int): Resource<Keywords> {
        val response = try {
            api.getShowKeywords(showId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getSeasonDetails(showId: Int, seasonNumber: Int): Resource<SeasonDetails> {
        val response = try {
            println("API Received ID: $showId, seasonN: $seasonNumber")

            api.getSeasonDetails(showId, seasonNumber)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

}