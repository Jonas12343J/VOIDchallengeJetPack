package com.example.voidchallengejetpack

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.voidchallengejetpack.showdetails.ShowDetailsScreen
import com.example.voidchallengejetpack.showslist.ShowListViewModel
import com.example.voidchallengejetpack.showslist.TVShowListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

// TODO talvez guardar os populares no primeiro request e apenas refazer o pedido se for dado refresh: mais rapido

var tvShowGenresList by mutableStateOf(mutableMapOf<Int, String>())
var displayedText by mutableStateOf("Popular right now:")


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel by viewModels<MainViewModel>()
    private val showsViewModel by viewModels<ShowListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !showsViewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L

                val fadeOut = ObjectAnimator.ofFloat(
                    screen.view,
                    View.ALPHA,
                    1f,
                    0f
                )
                fadeOut.interpolator = AccelerateInterpolator()
                fadeOut.duration = 1000L

                fadeOut.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
                fadeOut.start()

            }
        }

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = TVShowListMainScreen,
            ) {
                composable<TVShowListMainScreen> {
                    TVShowListScreen(navController = navController, viewModel = showsViewModel)
                }

                composable<TVShowDetailsScreen> {
                    val args = it.toRoute<TVShowDetailsScreen>()
                    ShowDetailsScreen(showId = args.id, navHostController = navController)
                    //TVShowDetails(tvShowId = args.id, viewModel = showsViewModel)
                }
            }
        }
    }
}

// Main Screen
@Serializable
object TVShowListMainScreen

// Second Screen
@Serializable
data class TVShowDetailsScreen(
    val id: Int
)

/*
fun setShowList(response: String) : List<TVShow> {
    val results = extractResults("results", response)
    if (results != null) {
        val tvShows = mutableListOf<TVShow>()
        for (i in 0 until results.length()) {
            val tvShowResult = results.getJSONObject(i)

            val tvShow = TVShow(
                id = tvShowResult.getInt("id"),
                title = tvShowResult.getString("name"),
                imagePosterURL = tvShowResult.getString("poster_path"),
                imageBackgroundURL = tvShowResult.getString("backdrop_path"),
                date = tvShowResult.getString("first_air_date"),
                genres = tvShowResult.getString("genre_ids"),
                description = tvShowResult.getString("overview"),
                rating = tvShowResult.getDouble("vote_average"),
                plus18 = tvShowResult.getBoolean("adult")
            )

            tvShows.add(tvShow)
        }
        // Pass the list of shows to the callback function
        return tvShows
    } else {
        println("Failed to extract results")
    }
    return emptyList()
}

fun setGenres(response: String) : MutableMap<Int, String> {
    val results = extractResults("genres", response)
    if (results != null) {
        val genres = mutableMapOf<Int, String>()
        for (i in 0 until results.length()) {
            val genre = results.getJSONObject(i)
            try {
                genres[genre.getInt("id")] = genre.getString("name")
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return genres
    } else {
        println("Failed to extract results")
    }
    return mutableMapOf()
}
*/


/*
/**
 * Extracts the "tag" array from the JSON response
 */
fun extractResults(tag: String, responseBody: String?): JSONArray? {
    if (responseBody.isNullOrEmpty()) return null

    try {
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getJSONArray(tag)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
*/