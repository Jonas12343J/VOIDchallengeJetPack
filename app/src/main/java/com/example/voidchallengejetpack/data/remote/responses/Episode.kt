package com.example.voidchallengejetpack.data.remote.responses

data class Episode(
    val air_date: String,
    val crew: List<Any> = emptyList(),
    val episode_number: Int = 0,
    val episode_type: String = "",
    val guest_stars: List<GuestStar> = emptyList(),
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val production_code: String = "",
    val runtime: Int = 0,
    val season_number: Int = 0,
    val show_id: Int = 0,
    val still_path: String = "",
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
)