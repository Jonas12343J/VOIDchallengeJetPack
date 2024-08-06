package com.example.voidchallengejetpack.showslist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voidchallengejetpack.data.models.ShowListEntry
import com.example.voidchallengejetpack.data.remote.responses.Genre
import com.example.voidchallengejetpack.repository.ShowRepository
import com.example.voidchallengejetpack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowListViewModel @Inject constructor(
    private val repository: ShowRepository
) : ViewModel() {

    //private var curPage = 0
    private var _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    var showsList = mutableStateOf<List<ShowListEntry>>(listOf())
    var genresList = mutableStateOf<List<Genre>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var cachedTVShowList = listOf<ShowListEntry>()
    var isSearching = mutableStateOf(false)

    var emptySearch = mutableStateOf(true)

    val searchText = mutableStateOf("")

    init {
        loadGenres()
        loadShows()
        _isReady.value = true
    }

    fun searchShowList(query: String): Boolean {

        // CPU operation
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.value = true
            when(val result = repository.searchShows(query)) {
                is Resource.Success -> {
                    val showEntries = result.data!!.results.mapIndexed { _, entry ->
                        val id = entry.id
                        val title = entry.name
                        val posterURL = entry.poster_path ?: ""
                        val backgroundURL = entry.backdrop_path ?: ""
                        val date = entry.first_air_date ?: ""
                        val genres = entry.genre_ids ?: ""
                        val description = entry.overview ?: ""
                        val rating = entry.vote_average ?: ""
                        val adult = entry.adult ?: ""

                        ShowListEntry(
                            id,
                            title,
                            posterURL,
                            backgroundURL,
                            date,
                            genres.toString(),
                            description,
                            rating as Double,
                            adult as Boolean
                        )
                    }

                    loadError.value = ""
                    isLoading.value = false
                    showsList.value = showEntries

                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }
            }
        }
        emptySearch.value = showsList.value.isEmpty()
        return !emptySearch.value
    }

    fun loadShows() : Boolean {
        if (cachedTVShowList.isNotEmpty()) {
            showsList.value = cachedTVShowList
        }
        else {
            viewModelScope.launch {
                isLoading.value = true
                when (val result = repository.getPopularShows()) {
                    is Resource.Success -> {

                        val showEntries = result.data!!.results.mapIndexed { _, entry ->
                            val id = entry.id
                            val title = entry.name
                            val posterURL = entry.poster_path ?: ""
                            val backgroundURL = entry.backdrop_path ?: ""
                            val date = entry.first_air_date ?: ""
                            val genres = entry.genre_ids ?: ""
                            val description = entry.overview ?: ""
                            val rating = entry.vote_average ?: ""
                            val adult = entry.adult ?: ""

                            ShowListEntry(
                                id,
                                title,
                                posterURL,
                                backgroundURL,
                                date,
                                genres.toString(),
                                description,
                                rating as Double,
                                adult as Boolean
                            )
                        }

                        loadError.value = ""
                        isLoading.value = false
                        showsList.value = showEntries

                    }

                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }

                    is Resource.Loading -> {
                        isLoading.value = true
                    }
                }
            }
        }
        return showsList.value.isNotEmpty()
    }

    fun loadGenres() : List<Genre> {
        isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getShowGenres()) {
                is Resource.Success -> {
                    val genres = result.data!!.genres.mapIndexed { _, genre ->
                        val genreID = genre.id
                        val genreName = genre.name

                        Genre(genreID, genreName)
                    }

                    genresList.value = genres
                    loadError.value = ""
                    isLoading.value = false
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {
                    isLoading.value = true
                }
            }
        }
        return genresList.value
    }
}