package com.example.voidchallengejetpack.data.models

import androidx.compose.runtime.Immutable

@Immutable
class ShowListEntry(
    val id: Int,
    val title: String,
    val imagePosterURL: String,
    val imageBackgroundURL: String,
    val date: String,
    val genres: String,
    val description: String,
    val rating: Double,
    val adult: Boolean
)