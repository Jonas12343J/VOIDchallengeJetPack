package com.example.voidchallengejetpack.data.remote.responses

data class ShowsResultList(
    val page: Int,
    val results: List<ShowResult>,
    val total_pages: Int,
    val total_results: Int
)