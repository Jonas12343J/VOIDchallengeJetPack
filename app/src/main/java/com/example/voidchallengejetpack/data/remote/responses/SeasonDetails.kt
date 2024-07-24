package com.example.voidchallengejetpack.data.remote.responses

data class SeasonDetails(
    val _id: String,
    val air_date: String,
    val episodes: List<Episode>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: Any,
    val season_number: Int,
    val vote_average: Double
)