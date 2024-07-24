package com.example.voidchallengejetpack

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
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

    init {
        loadGenres()
        loadShows()
        _isReady.value = true
    }

    fun searchShowList(query: String) : Boolean {

        // CPU operation
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.value = true
            when(val result = repository.searchShows(query)) {
                is Resource.Success -> {
                    val showEntries = result.data!!.results.mapIndexed { index, entry ->
                        val id = entry.id
                        val title = entry.name ?: ""
                        val posterURL = entry.poster_path ?: ""
                        val backgroundURL = entry.backdrop_path ?: ""
                        val date = entry.first_air_date ?: ""
                        val genres = entry.genre_ids ?: ""
                        val description = entry.overview ?: ""
                        val rating = entry.vote_average ?: ""
                        val adult = entry.adult ?: ""

                        // TODO genres string & empty values
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
            }
        }
        return showsList.value.isEmpty()
    }

    fun loadShows() {
        if (cachedTVShowList.isNotEmpty()) {
            showsList.value = cachedTVShowList
        }
        else {
            viewModelScope.launch {
                isLoading.value = true
                when (val result = repository.getPopularShows()) {
                    is Resource.Success -> {

                        val showEntries = result.data!!.results.mapIndexed { index, entry ->
                            val id = entry.id
                            val title = entry.name ?: ""
                            val posterURL = entry.poster_path ?: ""
                            val backgroundURL = entry.backdrop_path ?: ""
                            val date = entry.first_air_date ?: ""
                            val genres = entry.genre_ids ?: ""
                            val description = entry.overview ?: ""
                            val rating = entry.vote_average ?: ""
                            val adult = entry.adult ?: ""

                            // TODO genres string & empty values
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
                }
            }
        }
    }

    fun loadGenres() {
        isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getShowGenres()) {
                is Resource.Success -> {
                    val genres = result.data!!.genres.mapIndexed { index, genre ->
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
            }
        }
    }

    fun getShowDetails(showID: Int) {
        viewModelScope.launch {
            when (val result = repository.getShowDetails(showID)) {
                is Resource.Success -> {
                    // TODO
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                }
            }
        }
    }

    // TODO might be useful for a lighter UI
    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}