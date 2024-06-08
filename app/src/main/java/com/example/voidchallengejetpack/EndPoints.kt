package com.example.voidchallengejetpack

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


fun getPopularShowsAsync(onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {

    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/tv/popular?api_key=$API_KEY&language=en-US&page=1")
        .build()

    // Use coroutines to perform the network request asynchronously
    GlobalScope.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}

fun performSearchAsync(scope: CoroutineScope, query: String, onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {
    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/search/tv?language=en-US&page=1&api_key=$API_KEY&query=$query")
        .build()

    // Use coroutines to perform the network request asynchronously
    //GlobalScope.launch(Dispatchers.IO) {
    scope.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}

fun getShowGenresAsync(onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {

    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/genre/tv/list?api_key=$API_KEY&language=en-US")
        .build()

    // Use coroutines to perform the network request asynchronously
    GlobalScope.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}

fun getShowDetailsAsync(scope: CoroutineScope?, showID: Int, onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {

    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        //.url("https://api.themoviedb.org/3/tv/$showID?api_key=$API_KEY&language=en-US")
        // This URL includes everything relevant in the previous one plus the cast members
        .url("https://api.themoviedb.org/3/tv/$showID?api_key=$API_KEY&language=en-US&append_to_response=aggregate_credits")
        .build()

    // Use coroutines to perform the network request asynchronously
    //GlobalScope.launch(Dispatchers.IO) {
    scope?.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}


fun getKeyWordsAsync(scope: CoroutineScope, showID: Int, onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {

    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/tv/$showID/keywords?api_key=$API_KEY&language=en-US")
        .build()

    // Use coroutines to perform the network request asynchronously
    //GlobalScope.launch(Dispatchers.IO) {
    scope.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}

fun getSeasonDetailsAsync(showID: Int, seasonID: Int, onSuccess: (String) -> Unit, onFailure: (IOException) -> Unit) {

    fetchingResults = true

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.themoviedb.org/3/tv/$showID/season/$seasonID?api_key=$API_KEY&language=en-US")
        .build()

    // Use coroutines to perform the network request asynchronously
    GlobalScope.launch(Dispatchers.IO) {
        try {
            // Execute the request on the background thread
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Switch back to the main thread to handle UI updates
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    // Handle successful response
                    onSuccess(responseBody)
                } else {
                    // Handle unsuccessful response
                    onFailure(IOException("Unexpected response: ${response.code}"))
                }
                fetchingResults = false
            }
        } catch (e: IOException) {
            fetchingResults = false
            // Handle network request failure
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}