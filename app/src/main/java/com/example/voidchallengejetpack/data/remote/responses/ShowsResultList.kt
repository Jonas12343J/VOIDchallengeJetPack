package com.example.voidchallengejetpack.data.remote.responses

data class ShowsResultList(
    val page: Int,
    var results: List<ShowResult>, // var for sorting
    val total_pages: Int,
    val total_results: Int
)