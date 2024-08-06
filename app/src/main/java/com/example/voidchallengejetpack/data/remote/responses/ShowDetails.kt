package com.example.voidchallengejetpack.data.remote.responses

data class ShowDetails(
    val adult: Boolean,
    val aggregate_credits: AggregateCredits,
    val backdrop_path: Any,
    val created_by: List<Creator>,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: Any,
    val last_episode_to_air: Any,
    val name: String,
    val networks: List<Any>,
    val next_episode_to_air: Episode?,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: Any,
    val production_companies: List<ProductionCompanies>,
    val production_countries: List<Any>,
    val seasons: List<SeasonDetails>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)