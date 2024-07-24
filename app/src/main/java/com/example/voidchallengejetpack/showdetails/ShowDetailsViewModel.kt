package com.example.voidchallengejetpack.showdetails

import androidx.lifecycle.ViewModel
import com.example.voidchallengejetpack.data.remote.responses.Keywords
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.data.remote.responses.ShowDetails
import com.example.voidchallengejetpack.repository.ShowRepository
import com.example.voidchallengejetpack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val repository: ShowRepository
)  :ViewModel() {

    suspend fun getShowDetails(showId: Int): Resource<ShowDetails> {
        return repository.getShowDetails(showId)
    }

    suspend fun getShowKeywords(showId: Int): Resource<Keywords> {
        return repository.getShowKeywords(showId)
    }

    suspend fun getSeasonDetails(showId: Int, seasonNumber: Int): Resource<SeasonDetails> {
        println("repository Received ID: $showId, seasonN: $seasonNumber")
        return repository.getSeasonDetails(showId, seasonNumber)
    }
}