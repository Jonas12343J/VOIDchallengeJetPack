package com.example.voidchallengejetpack.data.remote.responses

data class Episode(
    val air_date: String,
    val crew: List<Any>,
    val episode_number: Int,
    val episode_type: String,
    val guest_stars: List<GuestStar>,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val runtime: Any,
    val season_number: Int,
    val show_id: Int,
    val still_path: Any,
    val vote_average: Double,
    val vote_count: Int
)