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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voidchallengejetpack.CastMemberLayout
import com.example.voidchallengejetpack.EpisodeLayout
import com.example.voidchallengejetpack.R
import com.example.voidchallengejetpack.data.remote.responses.Keywords
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.doubleColorText
import com.example.voidchallengejetpack.myExposedDropdownMenuBox
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.contracts.Effect

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

    Box(
        Modifier
            .background(Color.Black)
    ) {
        // LazyColumn(
        //    modifier = Modifier
        //       .fillMaxSize()
                    //) {
            //item {
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

                ////////////////////////////////////// todo re add ////////////////////////////

                /*
                Column (
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(start = 24.dp, end = 24.dp, top = 150.dp)
                    ){

                        Spacer(modifier = Modifier.height(16.dp))


                        showDetails.data?.name?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        showDetails.data?.tagline?.let {
                            Text(
                                text = it,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            showDetails.data?.adult?.let {
                                if (it) {
                                    Image(
                                        painter = painterResource(id = R.drawable.plus18),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }

                            showDetails.data?.vote_average?.let {
                                if (it != 0.0) {
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = "${"%.2f".format(it)} ★",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            }

                            showDetails.data?.first_air_date?.let {
                                if (it != "") {
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = it.substring(0, 4),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            }

                            showDetails.data?.episode_run_time?.let {
                                println(it)
                                if (it.isNotEmpty() && it[0] != 0) {
                                    Spacer(Modifier.width(8.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                                        contentDescription = null,
                                        Modifier
                                            .size(24.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Text(
                                        text = "${it[0]} min",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            }

                        }
                        showDetails.data?.overview?.let {
                            if (it != "") {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = it,
                                    color = colorResource(id = R.color.grayish_text),
                                    fontSize = 12.sp,
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        showDetails.data?.type?.let {
                            if (it != "") {
                                Spacer(modifier = Modifier.height(16.dp))
                                val text = doubleColorText(String.format("%-22s", "Type:"), it)
                                Text(
                                    text = text,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                            }
                        }

                        showDetails.data?.origin_country?.let {
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

                        // TODO review
                        showDetails.data?.genres?.let {
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

                        showDetails.data?.first_air_date?.let {
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

                        // TODO review
                        showDetails.data?.spoken_languages?.let {
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

                        // TODO review
                        showDetails.data?.created_by?.let {
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

                        // TODO review
                        showDetails.data?.production_companies?.let {
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

                        showDetails.data?.status?.let {
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

                    }
                    */

                    ////////////////////////////////////// todo re add ////////////////////////////

                    /*showDetails.data?.keywords?.let {
                        if (it.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val text = doubleColorText(String.format("%-17s", "Keywords:"), it.joinToString(", ") { it.name })
                            Text(
                                text = text,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }*/


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

                        /*
                        Column(
                            Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(res.screenPercentage)
                                .padding(start = 24.dp, end = 24.dp, top = 150.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {


                            Spacer(modifier = Modifier.height(16.dp))

                            if (res.name != "") {

                                Text(
                                    text = res.name,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }


                            if (res.tagline != "") {

                                Text(
                                    text = "\t" + res.tagline,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontStyle = FontStyle.Italic
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (res.adult) {
                                    Image(
                                        painter = painterResource(id = R.drawable.plus18),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)

                                    )
                                }

                                if (res.voteAverage != 0.0) {
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = "${res.voteAverage} ★",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }

                                if (res.releaseDate != "") {
                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        text = res.releaseDate.substring(0, 4),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }

                                if (res.episodeRunTime != 0) {
                                    Spacer(Modifier.width(8.dp))

                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                                        contentDescription = null,
                                        Modifier
                                            .size(24.dp)
                                            .padding(end = 4.dp)
                                    )

                                    Text(
                                        text = "${res.episodeRunTime} min",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            }

                            if (res.overview != "") {

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = res.overview,
                                    color = colorResource(id = R.color.grayish_text),
                                    fontSize = 12.sp,
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (res.type != "") {

                                Spacer(modifier = Modifier.height(16.dp))

                                text =
                                    doubleColorText(String.format("%-22s", "Type:"), res.type)

                                Text(
                                    text = text,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                            }

                            if (res.originCountry != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-20s", "Country:"),
                                    res.originCountry
                                )

                                Text(
                                    text = text,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                            }

                            if (res.genresString != "") {
                                text =
                                    doubleColorText(
                                        String.format("%-20s", "Genres:"),
                                        res.genresString
                                    )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                            }

                            if (res.releaseDate != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = String.format(
                                        "%-19s",
                                        "Release:"
                                    ) + SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(
                                        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(
                                            res.releaseDate
                                        )!!
                                    ),
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.grayish_text)
                                )
                            }

                            if (res.spokenLanguagesString != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-16s", "Languages:"),
                                    res.spokenLanguagesString
                                )

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            if (res.creatorsString != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-19s", "Creators:"),
                                    res.creatorsString
                                )

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            if (res.productionString != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-17s", "Production:"),
                                    res.productionString
                                )

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            if (res.status != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-21s", "Status:"),
                                    res.status
                                )

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            if (res.keywordsString != "") {

                                Spacer(modifier = Modifier.height(8.dp))

                                text = doubleColorText(
                                    String.format("%-17s", "Keywords:"),
                                    res.keywordsString
                                )

                                Text(
                                    text = text,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SEASONS
                            if (res.seasons != null) {

                                val size = res.seasons!!.length()

                                val selectedSeason = myExposedDropdownMenuBox(size)

                                getSeasonDetailsAsync(
                                    showID = tvShowId,
                                    seasonID = selectedSeason,
                                    onSuccess = { response ->
                                        seasonDetails = JSONObject(response)
                                    },
                                    onFailure = { exception ->
                                        // Handle network request failure
                                        println("Error: ${exception.message}")
                                    }
                                )

                                val episodes = seasonDetails.optJSONArray("episodes")

                                if (episodes != null) {
                                    episodeList = emptyList()
                                    for (i in 0 until episodes.length()) {
                                        val episode = episodes.getJSONObject(i)
                                        episodeList += Episode(
                                            name = episode.optString("name", ""),
                                            runtime = episode.optInt("runtime", 0),
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(modifier = Modifier.fillMaxWidth()) {
                                items(
                                    items = episodeList,
                                    key = { it.id }
                                ) { episode ->
                                    EpisodeLayout(episode = episode)
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            if (res.lastEpisodeToAir != null) {
                                Text(
                                    text = "Most recent episode",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    LastEpisodeLayout(
                                        name = res.lastEpisodeToAir.optString("name", ""),
                                        runtime = res.lastEpisodeToAir.optInt("runtime", 0),
                                        stillPath = res.lastEpisodeToAir.optString(
                                            "still_path", seasonPoster
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    val episodeNumber =
                                        res.lastEpisodeToAir.optInt("episode_number", 0)
                                    val episodeString =
                                        if (episodeNumber != 0) "Ep nº$episodeNumber: " else "Last episode: "
                                    val epOverview =
                                        res.lastEpisodeToAir.optString("overview", "")
                                    if (epOverview != "") Text(
                                        text = doubleColorText(
                                            prefix = episodeString,
                                            suffix = epOverview
                                        ),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            if (res.cast != null) {
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Main Cast", color = Color.White, fontSize = 16.sp)

                                Spacer(modifier = Modifier.height(12.dp))

                                castMembers = emptyList()
                                for (i in 0 until minOf((res.cast.length()), 10)) {
                                    val castMember = res.cast.getJSONObject(i)
                                    castMembers += Person(
                                        name = castMember.getString("name"),
                                        profilePath = castMember.getString("profile_path"),
                                        roles = castMember.getJSONArray("roles")
                                            .getJSONObject(0).getString("character")
                                    )
                                }
                            }

                            LazyRow(modifier = Modifier.fillMaxWidth()) {
                                items(
                                    items = castMembers,
                                    key = { it.id }
                                ) { castMember ->
                                    CastMemberLayout(castMember = castMember)
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                        }

                    }*/
                //} items

        //} lazy column
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
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(8.dp, 16.dp)
                .clickable {
                    navHostController.popBackStack()
                }
        )
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
                showKeywords = showKeywords.data!!,
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
    modifier: Modifier = Modifier ,
    showKeywords: Keywords? = null,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    // TODO TERRIBLE!!! KEEPS RECOMPOSING
    if (showKeywords != null) {
        keywords = ""
        for (keyword in showKeywords.results) {
            if (keywords != "") keywords += ", "
            keywords += keyword.name
        }
    }

    Column (
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp)
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




                /*
                if (seasonDetails is Resource.Success) {
                    //println("result: ${seasonDetails.data!!.season_number}")
                    SeasonEpisodeListSection(
                        seasonDetails = seasonDetails,
                        modifier = modifier
                    )

                }
                else if (seasonDetails is Resource.Error) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = seasonDetails.message!!,
                            color = Color.Red,
                            modifier = modifier,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                    }
                }
                else {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.themeColorSec),
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
                */

                /*

                when (seasonDetails) {
                    is Resource.Success -> {

                        if (previousSeasonNumber != selectedSeasonNumber) {
                            previousSeasonNumber = selectedSeasonNumber
                            //return@let
                            seasonDetails = produceState<Resource<SeasonDetails>>(initialValue = Resource.Loading()) {
                                value = viewModel.getSeasonDetails(showDetails.id, selectedSeasonNumber)
                            }.value
                        }
                        else {
                            println("result: ${seasonDetails.data!!.season_number}")
                            SeasonEpisodeListSection(
                                seasonDetails = seasonDetails,
                                modifier = modifier
                            )

                        }
                    }

                    is Resource.Error -> {
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = seasonDetails.message!!,
                                color = Color.Red,
                                modifier = modifier,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp
                            )
                        }
                    }
                    is Resource.Loading -> {

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = colorResource(id = R.color.themeColorSec),
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }
                }
*/
            }
        }
        //  TODO CAST
        showDetails.aggregate_credits?.cast?.let {
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
