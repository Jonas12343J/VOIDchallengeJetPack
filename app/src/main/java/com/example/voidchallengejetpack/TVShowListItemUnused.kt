package com.example.voidchallengejetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_200
//import com.example.voidchallengejetpack.data.models.TVShowListEntry
//import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale

@Immutable
data class TVShow(
    val id: Int,
    val title: String,
    val imagePosterURL: String,
    val imageBackgroundURL: String,
    val date: String,
    val genres: String,
    val description: String,
    val rating: Double,
    val plus18: Boolean

)

/*
@Composable
fun TVShowItem(tvShow: TVShow, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                navController.navigate("tvShowDetails/${tvShow.id}")
            }
    ) {

        Row( // single row
            modifier = Modifier
                .background(color = colorResource(id = R.color.themeColorPrim))
                .height(160.dp),

            horizontalArrangement = Arrangement.Center,

        ) {
            if (tvShow.imagePosterURL != "null") {
                RemoteImage( // Poster

                    url = IMG_PATH + tvShow.imagePosterURL,
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.themeColorPrim))
                        .fillMaxHeight()
                        .width(90.dp),

                    placeholderResId = R.drawable.img_placeholder
                )
            }
            else
                Image(
                    painter = painterResource(id = R.drawable.img_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                )
            /*
            // For preview purpose
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
            )
            */

            Box (
                modifier = Modifier
                    .fillMaxSize()

            ){
                RemoteImage(
                    url = IMG_PATH + tvShow.imageBackgroundURL,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )

                // For preview purpose
                /*Image( // Poster
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f)
            )*/


                Column(
                    // Column for remaining info
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tvShow.title,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.white)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = tvShow.plus18.toString(),
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
                            text = tvShow.date,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.grayish_text)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        /*val genreIds = tvShow.genres.split(", ") // split the genres string into a list of ids
                        val genreNames = genreIds.mapNotNull { id ->
                            tvShowGenresList[id.toInt()] // map each id to its corresponding genre name
                        }
                        val genresString = genreNames.joinToString(", ") // join the genre names back into a string
*/
                        Text(
                            text = tvShow.genres,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.grayish_text)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Description",
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.white)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        RatingBar(
                            maxStars = 5,
                            rating = tvShow.rating,
                            modifier = Modifier.size(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),

                        maxLines = 2,
                        text = tvShow.description,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.grayish_text)
                    )
                    /*
                    Image(
                        painter = painterResource(id = R.drawable.movie_poster),
                        contentDescription = null,
                        modifier = Modifier
                            .height(160.dp)
                            .padding(end = 8.dp)
                            .alpha(0.2f)
                    )*/
                }


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
*/

@Composable
fun TVShowItemLighter(tvShow: TVShow, navController: NavHostController) {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.themeColorPrim))
            .padding(4.dp)
            .height(160.dp)
            .clickable {
                navController.navigate(
                    "tv_show_details_screen/${tvShow.id}"
                    /*TVShowDetailsScreen(
                        id = tvShow.id,
                    )*/
                )
            }

    ) {
        AsyncImage(
            model = IMG_PATH_200 + tvShow.imagePosterURL,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.3f)
                .width(90.dp),
            placeholder = painterResource(id = R.drawable.img_placeholder),
        )
            /*if (tvShow.imagePosterURL != "null") {
                RemoteImage( // Poster

                    url = IMG_PATH_200 + tvShow.imagePosterURL,
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.themeColorPrim))
                        .fillMaxHeight()
                        .fillMaxWidth(.3f)
                        .width(90.dp),

                    placeholderResId = R.drawable.img_placeholder // TODO with skeleton view, no need for this
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                )
            }
*/

        Box (Modifier.fillMaxWidth()){
           /* RemoteImage(
            url = IMG_PATH_300 + tvShow.imageBackgroundURL,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )*/
            /*Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f)
                    .alpha(0.2f)
            )*/


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {


                Text(
                    text = tvShow.title,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.white)
                )

                Spacer(modifier = Modifier.height(4.dp))

                var genresString by remember {
                    mutableStateOf("")
                }

                genresString = ""

                for (genre in tvShowGenresList) {
                    if (tvShow.genres.contains(genre.key.toString())) {
                        if (genresString != "")
                            genresString += ", "
                        genresString += genre.value
                    }
                }

                Text(
                    text = tvShow.date + "    " + genresString,
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.grayish_text)
                )

                Spacer(modifier = Modifier.height(8.dp))


                RatingBar(
                    maxStars = 5,
                    rating = tvShow.rating / 2, // 0 - 10
                    modifier = Modifier.size(8.dp),
                    string = String.format(Locale.US, "(%.2f ★)", tvShow.rating)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (tvShow.description != "") {
                    Text(
                        text = "Description",
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.white)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = tvShow.description,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.grayish_text),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTVShowItem() {
    val tvShow = TVShow(
        id = 123456,
        title = "Sample TV Show Title",
        imagePosterURL = "https://sample-url.com/poster.jpg",
        imageBackgroundURL = "https://sample-url.com/background.jpg",
        date = "2024-05-07",
        genres = "Drama, Comedy",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        rating = 7.39,
        plus18 = true
    )
    //TVShowItemLighter(tvShow = tvShow, navController = rememberNavController())
}

