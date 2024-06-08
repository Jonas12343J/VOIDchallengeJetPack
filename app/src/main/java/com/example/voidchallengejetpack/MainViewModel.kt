package com.example.voidchallengejetpack

import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel()   {

    private var _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {

            getShowGenresAsync(
                onSuccess = { response ->
                    tvShowGenresList = setGenres(response)
                },
                onFailure = { exception ->
                    // Handle network request failure
                    println("Error: ${exception.message}")
                    displayedText = "Check your internet connection..."
                }
            )

            getPopularShowsAsync(
                onSuccess = { response ->
                    // Handle successful response and update UI
                    // You need to implement this part based on your UI and data handling
                    tvShowList = setShowList(response)
                    displayedText = if (tvShowList.isNotEmpty()) {
                        "Popular right now:"
                    } else {
                        "Nothing popular right now"
                    }
                },
                onFailure = { exception ->
                    // Handle network request failure
                    println("Error: ${exception.message}")
                    displayedText = "Check your internet connection..."
                }
            )
            _isReady.value = true
        }
    }


}