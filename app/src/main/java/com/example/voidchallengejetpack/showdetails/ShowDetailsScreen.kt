package com.example.voidchallengejetpack.showdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.voidchallengejetpack.data.remote.responses.ShowDetails
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_400
import com.example.voidchallengejetpack.util.Resource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voidchallengejetpack.CastMemberLayout
import com.example.voidchallengejetpack.EpisodeLayout
import com.example.voidchallengejetpack.NextEpisodeLayout
import com.example.voidchallengejetpack.R
import com.example.voidchallengejetpack.data.remote.responses.Keywords
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.doubleColorText
import com.example.voidchallengejetpack.myExposedDropdownMenuBox
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

var keywords by mutableStateOf("")

@Composable
fun ShowDetailsScreen(
    showId: Int,
    navHostController: NavHostController,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val showDetails = produceState<Resource<ShowDetails>>(initialValue = Resource.Loading()) {
        value = viewModel.getShowDetails(showId)
    }.value


    val showKeywords = produceState<Resource<Keywords>>(initialValue = Resource.Loading()) {
        value = viewModel.getShowKeywords(showId)
    }.value

    DisposableEffect(key1 = showId) {
        onDispose {
            keywords = ""
        }
    }

    Box(
        Modifier
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ) {

            if (showDetails is Resource.Success) {
                showDetails.data?.poster_path?.let {
                    SubcomposeAsyncImage(
                        model = IMG_PATH_400 + it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.75f)
                            .alpha(.5f),
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.75f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
            }

            ShowDetailsTopSection(
                navHostController = navHostController,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.2f)
                    .align(Alignment.TopCenter)
            )
            ShowDetailsStateWrapper(
                showDetails = showDetails,
                showKeywords = showKeywords,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp),
                loadingModifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ShowDetailsTopSection(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
    ) {

        // Prevents multiple clicks in a short time to pop the backstack to an undefined screen
        var isClickable by remember { mutableStateOf(true) }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(8.dp, 16.dp)
                .clickable(enabled = isClickable) {
                    isClickable = false
                    navHostController.popBackStack()
                }
            )
        LaunchedEffect(key1 = isClickable) {
            delay(2000)
            isClickable = true
        }
    }
}

@Composable
fun ShowDetailsStateWrapper(
    showDetails: Resource<ShowDetails>,
    showKeywords: Resource<Keywords>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(showDetails) {
        is Resource.Success -> {
            ShowDetailsSection(
                showDetails = showDetails.data!!,
                showKeywords = showKeywords.data,
                modifier = modifier
            )
        }
        is Resource.Error -> {
            Box(
                modifier = modifier

            ) {
                Text(
                    text = showDetails.message!!,
                    color = Color.Red,
                    modifier = modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
        is Resource.Loading -> {
            Box(
                modifier = modifier
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.themeColorSec),
                    modifier = loadingModifier
                )
            }
        }
    }
}

@Composable
fun ShowDetailsSection(
    showDetails: ShowDetails,
    modifier: Modifier = Modifier,
    showKeywords: Keywords?,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    if (showKeywords != null && showKeywords.results.isNotEmpty()) {
        keywords = ""
        for (keyword in showKeywords.results) {
            if (keywords != "") keywords += ", "
            keywords += keyword.name
        }
    }

    Column (
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.95f)
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            .verticalScroll(scrollState),
    ){

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = showDetails.name,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = showDetails.tagline,
            fontSize = 10.sp,
            color = Color.White,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showDetails.adult) {
                Image(
                    painter = painterResource(id = R.drawable.plus18),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)

                )
            }

            if (showDetails.vote_average != 0.0) {
                Spacer(Modifier.width(8.dp))

                Text(
                    text = "${showDetails.vote_average} ★",
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }

            if (showDetails.first_air_date != "") {
                Spacer(Modifier.width(8.dp))

                Text(
                    text = showDetails.first_air_date.substring(0, 4),
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }

            if (showDetails.episode_run_time.isNotEmpty() && showDetails.episode_run_time[0] != 0) {
                Spacer(Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = "${showDetails.episode_run_time[0]} min",
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }

        }

        showDetails.origin_country.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val text = doubleColorText(String.format("%-20s", "Country:"), it[0])
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }

        showDetails.type.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val text = doubleColorText(String.format("%-22s", "Type:"), it)
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }

        showDetails.origin_country.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val text = doubleColorText(String.format("%-20s", "Country:"), it[0])
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }

        showDetails.genres.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                val genres = mutableStateOf("")
                for (genre in it) {
                    if (genres.value != "") genres.value += ", "
                    genres.value += genre.name
                }

                val text = doubleColorText(String.format("%-20s", "Genres:"), genres.value)
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        showDetails.first_air_date.let {
            if (it != "") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format("%-19s", "Release:") + SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(
                        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(it)!!
                    ),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.grayish_text)
                )
            }
        }

        showDetails.spoken_languages.let {
            if (it.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))

                val spokenLanguages = mutableStateOf("")
                for (language in it) {
                    if (spokenLanguages.value != "") spokenLanguages.value += ", "
                    spokenLanguages.value += language.english_name
                }
                val text = doubleColorText(String.format("%-16s", "Languages:"), spokenLanguages.value)
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        showDetails.created_by.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                val creators = mutableStateOf("")
                for (creator in it) {
                    if (creators.value != "") creators.value += ", "
                    creators.value += creator.name
                }
                val text = doubleColorText(String.format("%-19s", "Creators:"), creators.value)
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        showDetails.production_companies.let {
            if (it.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))

                val productionCompanies = mutableStateOf("")
                for (company in it) {
                    if (productionCompanies.value != "") productionCompanies.value += ", "
                    productionCompanies.value += company.name
                }

                val text = doubleColorText(String.format("%-17s", "Production:"), productionCompanies.value)
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
        }

        showDetails.status.let {
            if (it != "") {
                Spacer(modifier = Modifier.height(8.dp))
                val text = doubleColorText(String.format("%-21s", "Status:"), it)
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        if (keywords.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            val text = doubleColorText(String.format("%-17s", "Keywords:"), keywords)
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color.White
            )
        }

        showDetails.seasons.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                var selectedSeasonNumber by remember { mutableIntStateOf(it[0].season_number) }
                var previousSeasonID by remember { mutableIntStateOf(it[0].id) }

                /*
                //var newSeasonRequest by remember { mutableStateOf(true) }

                //if (selectedSeasonID != previousSeasonID) {
                //        newSeasonRequest = true
                //   previousSeasonID = selectedSeasonID
                } */

                //selectedSeasonNumber = dropDownMenu(itemList = it)
                selectedSeasonNumber = myExposedDropdownMenuBox(itemList = it)

                /*
                val seasonDetails = produceState<Resource<SeasonDetails>>(initialValue = Resource.Loading()) {
                    value = viewModel.getSeasonDetails(showDetails.id, selectedSeasonNumber)
                }.value
                */
                if (selectedSeasonNumber != previousSeasonID) {
                    previousSeasonID = selectedSeasonNumber
                }

                else {
                    SeasonEpisodeListSection(
                        viewModel = viewModel,
                        selectedSeasonNumber = selectedSeasonNumber,
                        showID = showDetails.id
                    )
                }
            }
        }

        showDetails.next_episode_to_air.let {
            if (it != null) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Next Episode", color = Color.White, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                NextEpisodeLayout(it)
            }
        }

        showDetails.aggregate_credits.cast.let {
            if (it.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Main Cast", color = Color.White, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                val castScrollState = rememberScrollState()
                println("scroll state: $castScrollState")

                Row (
                    modifier = Modifier.horizontalScroll(castScrollState)
                ) {
                    for (i in 0 until minOf((it.size), 10)) {
                        val castMember = it[i]
                        CastMemberLayout(castMember = castMember)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Developed by Jóni Pereira as a VOID Software's challenge",
            color = Color.Gray,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic

        )
    }

}

@Composable
fun SeasonEpisodeListSection(
    viewModel: ShowDetailsViewModel = hiltViewModel(),
    selectedSeasonNumber: Int,
    showID: Int,
) {

    val scrollState = rememberScrollState()

    val seasonDetails = produceState<Resource<SeasonDetails>>(initialValue = Resource.Loading()) {
        value = viewModel.getSeasonDetails(showID, selectedSeasonNumber)
    }.value

    when (seasonDetails) {
        is Resource.Success -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .horizontalScroll(scrollState)
            ) {
                for (episode in seasonDetails.data!!.episodes) {
                    EpisodeLayout(
                        episode = episode,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }

        is Resource.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = seasonDetails.message!!,
                    color = Color.Red,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.themeColorSec),
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
    }

}
