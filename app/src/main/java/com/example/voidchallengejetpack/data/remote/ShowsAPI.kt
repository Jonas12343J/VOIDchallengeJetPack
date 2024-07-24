package com.example.voidchallengejetpack.data.remote

import com.example.voidchallengejetpack.data.remote.responses.Keywords
import com.example.voidchallengejetpack.data.remote.responses.ShowsResultList
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.data.remote.responses.ShowDetails
import com.example.voidchallengejetpack.data.remote.responses.ShowResult
import com.example.voidchallengejetpack.data.remote.responses.TVShowGenres
import com.example.voidchallengejetpack.util.Constants.APPEND_TO_RESPONSE
import com.example.voidchallengejetpack.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsAPI {

    @GET("tv/popular?api_key=$API_KEY&page=1")
    suspend fun getPopularShows(): ShowsResultList

    @GET("genre/tv/list?api_key=$API_KEY")
    suspend fun getGenres(): TVShowGenres

    @GET("search/tv?page=1&api_key=$API_KEY")
    suspend fun searchShows(
        @Query("query") query: String
    ): ShowsResultList

    @GET("tv/{showId}?api_key=$API_KEY&append_to_response=$APPEND_TO_RESPONSE")
    suspend fun getShowDetails(
        @Path("showId") showId: Int
    ): ShowDetails

    @GET("tv/{showId}/keywords?api_key=$API_KEY")
    suspend fun getShowKeywords(
        @Path("showId") showId: Int
    ): Keywords

    @GET("tv/{showId}/season/{seasonNumber}?api_key=$API_KEY")
    suspend fun getSeasonDetails(
        @Path("showId") showId: Int,
        @Path("seasonNumber") seasonNumber: Int
    ): SeasonDetails



}