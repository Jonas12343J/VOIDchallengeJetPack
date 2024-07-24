package com.example.voidchallengejetpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.voidchallengejetpack.data.models.ShowListEntry
import com.example.voidchallengejetpack.showslist.ShowListViewModel
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_200
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_300

@Composable
fun TVShowListScreen(
    navController: NavHostController,
    viewModel: ShowListViewModel = hiltViewModel()
) {

    var searchText by remember { mutableStateOf("") }

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
                        if (searchText.isNotEmpty()) { // Perform search and update movieList

                            /*performSearchAsync(scope = coroutineScope,
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
                                    displayedText = ""
                                }
                            )*/
                            displayedText = if (viewModel.searchShowList(searchText))
                                "Search results for: $searchText"
                            else
                                "No results found for: $searchText"

                        } else { // No search text, get popular shows
                            displayedText = "Popular right now:"
                            viewModel.loadShows()
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
            TVShowList(navController = navController, viewModel = viewModel)
        }
    }
}

// *****************************************************
@Composable
fun TVShowList(
    navController: NavHostController,
    viewModel: ShowListViewModel = hiltViewModel()
) {

    val showsList by remember { viewModel.showsList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.90f)
                .background(Color.Black),
        ) {
            val itemCount = if (showsList.size % 2 == 0) {
                showsList.size / 2
            } else {
                showsList.size / 2 + 1
            }

            items(
                itemCount,
                key = {
                    showsList[it].id
                }
            ) {
                if (it >= itemCount && !isLoading && !isSearching) {
                    viewModel.loadShows()
                }

                ShowListRow(rowIndex = it, entries = showsList, navController = navController)
            }

        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (isLoading) {
                CircularProgressIndicator(color = colorResource(id = R.color.themeColorSec))
            }

            if (!isLoading)
                Spacer(modifier = Modifier.height(40.dp))

            if (loadError.isNotEmpty()) {
                RetrySection(error = loadError) {
                    viewModel.loadGenres()
                    viewModel.loadShows()

                }
            }
        }
    }
}

// NEW TVShowListItem
/*
@Composable
fun TVShowEntry(
    entry: ShowListEntry,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ShowListViewModel = hiltViewModel()
) {
    val defaultDominantColor = colorResource(id = R.color.themeColorPrim)

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .padding(4.dp)
            .height(160.dp)
            .clickable {
                navController.navigate(
                    "tv_show_details_screen/${entry.id}"
                    /*
                    TVShowDetailsScreen(
                        id = entry.id,
                    )*/
                )
            }

    ) {
        if (entry.imagePosterURL != "null") {

            Box {

                AsyncImage(
                    model = entry.imagePosterURL,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.3f)
                        .width(90.dp),
                    placeholder = painterResource(id = R.drawable.img_placeholder),
                )
                CircularProgressIndicator(
                    color = colorResource(id = R.color.themeColorSec),
                    modifier = Modifier
                        .scale(.5f)
                )
            }

        }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {


                Text(
                    text = entry.title,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.white)
                )

                Spacer(modifier = Modifier.height(4.dp))

                var genresString by remember {
                    mutableStateOf("")
                }

                genresString = ""

                // TODO use genres in viewmodel
                for (genre in tvShowGenresList) {
                    if (entry.genres.contains(genre.key.toString())) {
                        if (genresString != "")
                            genresString += ", "
                        genresString += genre.value
                    }
                }

                Text(
                    text = entry.date + "    " + genresString,
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.grayish_text)
                )

                Spacer(modifier = Modifier.height(8.dp))


                RatingBar(
                    maxStars = 5,
                    rating = entry.rating / 2, // 0 - 10
                    modifier = Modifier.size(8.dp),
                    string = String.format(Locale.US, "(%.2f ★)", entry.rating)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (entry.description != "") {
                    Text(
                        text = "Description",
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.white)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = entry.description,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.grayish_text),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
    }
}
*/

@Composable
@Stable
fun TVShowEntry2PerRow(
    entry: ShowListEntry,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ShowListViewModel = hiltViewModel()
) {
    val defaultDominantColor = colorResource(id = R.color.themeColorPrim)

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier
            //.width(90.dp)
            //.height(160.dp)
            .aspectRatio(2f / 3f)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .padding(4.dp)
            .clickable {
                navController.navigate(
                    /*"tv_show_details_screen/${entry.id}"*/
                    TVShowDetailsScreen(
                        id = entry.id,
                    )
                )
            }
    ) {

            if(entry.imagePosterURL != "") {
                SubcomposeAsyncImage(
                    model = IMG_PATH_200 + entry.imagePosterURL,
                    filterQuality = FilterQuality.Medium,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    loading = {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.themeColorSec),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .scale(.5f)
                        )
                    },
                    )
        } else if (entry.imageBackgroundURL != "") {
            SubcomposeAsyncImage(
                model = IMG_PATH_300 + entry.imageBackgroundURL,
                filterQuality = FilterQuality.Medium,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                loading = {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.themeColorSec),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .scale(.5f)
                    )
                },
            )
        } else {
            Text(text = entry.title,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        if (entry.adult) {
            Image(
                painter = painterResource(id = R.drawable.plus18),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .align(Alignment.BottomStart)
            )
        }

        if (entry.rating > 0) {
            Column (
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){

                /*RatingBar(
                    maxStars = 5,
                    rating = entry.rating / 2, // 0 - 10
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(16.dp)
                        .background(Color.Black)
                )*/
            }
        }
    }
}

@Composable
@Stable
fun ShowListRow(
    rowIndex: Int,
    entries: List<ShowListEntry>,
    navController: NavHostController
) {
    Column {
       Row {
            TVShowEntry2PerRow(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                TVShowEntry2PerRow(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    Spacer (modifier = Modifier.height(8.dp))

}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {

        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShowListEntry() {
    TVShowEntry2PerRow(entry = ShowListEntry(
        id = 1,
        title = "Title",
        imagePosterURL = "https://image.tmdb.org/t/p/w500/6kbAMLteGO8yyewYau6bJ683sw7.jpg",
        date = "2021-09-01",
        genres = listOf("1", "2").toString(),
        description = "Description",
        rating = 8.0,
        adult = false,
        imageBackgroundURL = "https://image.tmdb.org/t/p/w500/6kbAMLteGO8yyewYau6bJ683sw7.jpg"
    ), navController = NavHostController(LocalContext.current))
}