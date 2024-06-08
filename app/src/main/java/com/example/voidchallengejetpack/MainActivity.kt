package com.example.voidchallengejetpack

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
//import com.example.voidchallengejetpack.data.models.TVShowListEntry
// dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects


// TODO talvez guardar os populares no primeiro request e apenas refazer o pedido se for dado refresh: mais rapido

const val API_KEY = "39a3c712614c598a6d5ca7a7c35a3ab1"
const val IMG_PATH_400 = "https://image.tmdb.org/t/p/w400"
const val IMG_PATH_300 = "https://image.tmdb.org/t/p/w300"
const val IMG_PATH_200 = "https://image.tmdb.org/t/p/w200"

var tvShowList by mutableStateOf<List<TVShow>>(emptyList())
var tvShowGenresList by mutableStateOf(mutableMapOf<Int, String>())
var displayedText by mutableStateOf("")
var fetchingResults by mutableStateOf(false)


//@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 1200L

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1200L

                val fadeOut = ObjectAnimator.ofFloat(
                    screen.view,
                    View.ALPHA,
                    1f,
                    0f
                )
                fadeOut.interpolator = AccelerateInterpolator()
                fadeOut.duration = 1200L

                zoomX.doOnEnd { screen.remove() }
                zoomY.doOnEnd { screen.remove() }
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
                startDestination = MainScreen,
            ) {
                composable<MainScreen> {
                    MyApp(navController)
                }

                composable<TVShowDetailsScreen> {
                    val args = it.toRoute<TVShowDetailsScreen>()
                    TVShowDetails(tvShowId = args.id)
                }
            }
        }
    }
}

// Main Screen
@Serializable
object MainScreen

// Second Screen
@Serializable
data class TVShowDetailsScreen(
    val id: Int
)

@Composable
fun MyApp(navController: NavHostController,
    ) {

    var searchText by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // loading data is finished or not
    val finish = remember {
        mutableStateOf(false)
    }

    Surface(color = colorResource(id = R.color.black)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)

            )

            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ){

                SearchBar(

                    onSearch = { newText ->
                        // Handle search functionality here
                        searchText = newText
                        println("Search text: $searchText")
                        if (searchText.isNotEmpty()) { // Perform search and update movieList

                            performSearchAsync(scope = coroutineScope,
                                searchText,
                                onSuccess = { response ->
                                    // Handle successful response and update UI
                                    tvShowList = setShowList(response)
                                    displayedText = if (tvShowList.isNotEmpty()) {
                                        "Search results for: $searchText"
                                    } else {
                                        "No results found for: $searchText"
                                    }
                                },
                                onFailure = { exception ->
                                    // Handle network request failure
                                    println("Error: ${exception.message}")
                                    displayedText = "Check your internet connection..."
                                }
                            )
                        } else { // No search text, get popular shows
                            getPopularShowsAsync(
                                onSuccess = { response ->
                                    // Handle successful response and update UI
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

                        }
                    }
                )
                }

            Text(
                text = displayedText,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)


            )

            if (fetchingResults) {
                displayedText = ""
                LoadingScreen()

            }

            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.90f)
                        .background(Color.Black),
                ) {
                    items(

                        items = tvShowList,
                        key = {
                            it.id
                        }
                    ) { tvShow ->
                        TVShowItemLighter(tvShow = tvShow, navController, fetchingResults)
                        HorizontalDivider()
                    }

                }
            }

            Text(
                text = "Developed by Jóni Pereira as a VOID's challenge",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(16.dp)

            )
        }
    }
}

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

/*
@Composable
fun MovieItem(title: String, date: String, genres: String, cast: String, description: String, rating: Int, duration: String, imagePosterURL: String, imageBackgroundURL: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row( // single row
            modifier = Modifier
                .background(color = colorResource(id = R.color.themeColorPrim))
                .height(160.dp)
                .padding(4.dp),

            horizontalArrangement = Arrangement.Center,


            ) {

            RemoteImage( // Poster
                url = imagePosterURL,
                modifier = Modifier
                    .fillMaxHeight()
                    .height(160.dp)
            )

            /*
            // For preview purpose
            Image(
                painter = painterResource(id = R.drawable.movie_poster),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
            )
             */

            Box() {
                Column(
                    // Column for remaining info
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.white)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        RatingBar(
                            maxStars = 5,
                            rating = rating
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = duration,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.grayish_text)
                        )

                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = date,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.grayish_text)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = genres,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.grayish_text)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Cast",
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.white)
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),

                        maxLines = 2,
                        text = cast,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.grayish_text)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Description",
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.white)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),

                        maxLines = 2,
                        text = description,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.grayish_text)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.movie_poster),
                        contentDescription = null,
                        modifier = Modifier
                            .height(160.dp)
                            .padding(end = 8.dp)
                            .alpha(0.2f)


                    )
                }

                RemoteImage(
                    url = imageBackgroundURL,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .align(Alignment.BottomStart)
                        .alpha(0.2f)
                )
                /*
                // For preview purpose
                Image( // Poster
                painter = painterResource(id = R.drawable.movie_poster),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
            )
                 */
            }
        }

        /*Image(
            painter = painterResource(id = R.drawable.movie_poster),
            contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .alpha(0.2f)


        )*/
    }
}

@Composable
fun RatingBar(
    maxStars: Int = 5,
    rating: Int,
) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= rating) colorResource(id = R.color.themeColorSec) else colorResource(id = R.color.black).copy(alpha = 0.8f),
                modifier = Modifier
                    .size(8.dp)
            )
        }
    }
}

@Composable
fun RemoteImage(url: String, modifier: Modifier = Modifier) {
    val painter: Painter = rememberImagePainter(url)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}
*/