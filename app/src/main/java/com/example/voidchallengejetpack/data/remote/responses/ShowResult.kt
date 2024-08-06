package com.example.voidchallengejetpack.data.remote.responses

data class ShowResult(
    val adult: Boolean = false,
    val backdrop_path: String = "",
    val first_air_date: String = "",
    val genre_ids: List<Int> = emptyList(),
    val id: Int,
    val name: String,
    val origin_country: List<String> = emptyList(),
    val original_language: String = "",
    val original_name: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
)