package com.example.voidchallengejetpack.showslist

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.example.voidchallengejetpack.R
import com.example.voidchallengejetpack.SearchBar
import com.example.voidchallengejetpack.TVShowDetailsScreen
import com.example.voidchallengejetpack.data.models.ShowListEntry
import com.example.voidchallengejetpack.displayedText
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_300
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_400

@Composable
fun TVShowListScreen(
    navController: NavHostController,
    viewModel: ShowListViewModel = hiltViewModel()
) {

    Surface(color = colorResource(id = R.color.black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
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
                        viewModel.searchText.value = newText

                        // Handle search functionality here
                        if (viewModel.searchText.value.isNotEmpty()) { // Perform search and update movieList
                            viewModel.searchShowList(viewModel.searchText.value)
                        } else { // No search text, get popular shows
                            viewModel.loadShows()
                        }
                    },
                    viewModel = viewModel

                )
            }

            Spacer(Modifier.height(16.dp))

            Column (
                Modifier
                .fillMaxWidth(.95f),
                horizontalAlignment = Alignment.Start
            ) {

                Text(text = displayedText, color = Color.White, fontSize = 20.sp)
            }

            Spacer(Modifier.height(16.dp))

            TVShowList(navController = navController, viewModel = viewModel, searchText = viewModel.searchText.value)

        }
    }
}

@Composable
fun TVShowList(
    navController: NavHostController,
    viewModel: ShowListViewModel = hiltViewModel(),
    searchText: String = ""
) {

    val showsList by remember { viewModel.showsList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()

    ) {
        if (!isLoading && !isSearching) {
            displayedText = if (searchText == "" && showsList.isNotEmpty()) {
                "Popular right now:"

            } else if (searchText != "" && showsList.isEmpty()) {
                "No results found for '${searchText}'..."

            } else if (searchText != "") {
                "Results for '${searchText}':"
            } else {
                ""
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f),
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

        Text(
            text = "Developed by Jóni Pereira as a VOID Software's challenge",
            color = Color.Gray,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .align(Alignment.BottomCenter)

        )

    }


}

@Composable
@Stable
fun TVShowEntry2PerRow(
    entry: ShowListEntry,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .background(colorResource(id = R.color.themeColorPrim))
            .padding(4.dp)
            .clickable {
                navController.navigate(
                    TVShowDetailsScreen(
                        id = entry.id,
                    )
                )
            }
    ) {

            if(entry.imagePosterURL != "") {
                SubcomposeAsyncImage(
                    model = IMG_PATH_400 + entry.imagePosterURL,
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
