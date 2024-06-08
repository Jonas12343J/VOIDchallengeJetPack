package com.example.voidchallengejetpack

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

var results by mutableStateOf(JSONObject())
var keywordsResponse by mutableStateOf(JSONObject())
private var castMembers by mutableStateOf<List<Person>>(emptyList())
private var seasonDetails by mutableStateOf(JSONObject())
var episodeList by mutableStateOf<List<Episode>>(emptyList())

@Immutable
data class Result(
    var posterPath: String,
    var name: String,
    var tagline: String,
    var releaseDate: String,
    var voteAverage: Double,
    var adult: Boolean,
    var overview: String,
    var type: String,
    var status: String,
    val cast: JSONArray?,
    val lastEpisodeToAir: JSONObject?,
    var seasonPoster: String?,
    var seasons: JSONArray?,
    var screenPercentage: Float,
    var episodeRunTime: Int,
    var originCountry: String,
    var genresString: String,
    var spokenLanguagesString: String,
    var creatorsString: String,
    var productionString: String,
    var keywordsString: String
)

// Available Information  ->  👑 - needed UI information || 💀 - extra UI information || 🦕 - irrelevant UI information
/*
👑 cast ✅ -> from aggregate_credits {} -> get cast -> [] -> each cast -> { name, profile_path, roles [{character}] }
👑 backdrop_path   ✅
👑 poster_path   ✅
👑 name   ✅
👑 vote_average   ✅
👑 adult   ✅
👑 episode_run_time -> [] -> int ✅
👑 genres -> [] -> {id, name} ✅
👑 overview   ✅
👑 spoken_languages -> [] -> {english_name, iso_639_1, name}  ✅
👑 status (on going, ended, ...) ✅
👑 tagline -> string  ✅
👑 type (scripted, talk show, ...) ✅
👑 created_by -> [] -> {id, credit_id, name, original_name, gender, profile_path} ✅
👑 seasons -> [] -> {air_date, episode_count, id, name, overview, poster_path, season_number}  ✅
👑 number_of_episodes  ✅
👑 number_of_seasons  ✅
👑 last_episode_to_air -> {} -> {air_date, episode_number, id, name, overview, production_code, season_number, show_id, still_path, vote_average, vote_count}
💀 last_air_date
💀 networks -> [] -> {name, id, logo_path, origin_country}
💀 first_air_date   ✅
💀 production_companies -> [] -> {id, logo_path, name, origin_country}
💀 production_countries -> [] -> {iso_3166_1, name}
💀 origin_country -> [] -> string
💀 next_episode_to_air -> {} -> {air_date, episode_number, id, name, overview, production_code, season_number, show_id, still_path, vote_average, vote_count}
💀 homepage
🦕 id
🦕 vote_count
🦕 popularity
 */


@Composable
fun TVShowDetails(tvShowId: Int) {

    // Layout yet to be defined
    var text: AnnotatedString
    //var textVar by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(tvShowId) {
        coroutineScope.launch {
            getShowDetailsAsync(
                scope = coroutineScope,
                showID = tvShowId,
                onSuccess = { response ->
                    //tvShow = setShowDetails(response)
                    results = JSONObject(response)
                },
                onFailure = { exception ->
                    // Handle network request failure
                    println("Error: ${exception.message}")
                }
            )

            getKeyWordsAsync(
                scope = coroutineScope,
                showID = tvShowId,
                onSuccess = { response ->
                    //tvShow = setShowDetails(response)
                    keywordsResponse = JSONObject(response)
                },
                onFailure = { exception ->
                    // Handle network request failure
                    println("Error: ${exception.message}")
                }
            )


        }

    }

    fetchingResults = true

    // Get the details
    val posterPath = results.optString("poster_path", "")
    val name = results.optString("name", "")
    val tagline = results.optString("tagline", "")
    val releaseDate = results.optString("first_air_date", "")
    val voteAverage = results.optDouble("vote_average", 0.0)
    val adult = results.optBoolean("adult", false)
    val overview = results.optString("overview", "")
    val type = results.optString("type", "")
    val status = results.optString("status", "")
    val cast = results.optJSONObject("aggregate_credits")?.optJSONArray("cast")
    val lastEpisodeToAir = results.optJSONObject("last_episode_to_air")

    val seasonsArray = results.optJSONArray("seasons")

    var seasonPoster = ""
    if (seasonsArray != null && seasonsArray.length() > 0) {
        if (seasonsArray.getJSONObject(0) != null)
            seasonPoster = seasonsArray.getJSONObject(0).optString("poster_path", "")
    }
    val seasons = results.optJSONArray("seasons")

    // Screen percentage
    var screenPercentage = 0.9f
    if (!adult)
        screenPercentage = 1f

    // Episode runtime
    val episodeRunTimeArray = results.optJSONArray("episode_run_time")
    val episodeRunTime = if (episodeRunTimeArray != null && episodeRunTimeArray.length() > 0) {
        episodeRunTimeArray.get(0)
    } else {
        0
    }

    // Origin country
    val country = results.optJSONArray("origin_country")

    val originCountry = if (country != null && country.length() > 0) {
        country.get(0).toString()
    } else {
        ""
    }

    // Genres
    var genresString by remember {
        mutableStateOf("")
    }

    genresString = ""
    val genres = results.optJSONArray("genres")

    if (genres != null && genres.length() > 0) {
        for (i in 0 until genres.length()) {
            val genre = genres.getJSONObject(i)
            if (genresString != "")
                genresString += ", "
            genresString += genre.getString("name")
        }
    }

    // Spoken languages
    val spokenLanguages = results.optJSONArray("spoken_languages")

    var spokenLanguagesString by remember {
        mutableStateOf("")
    }

    spokenLanguagesString = ""

    if (spokenLanguages != null && spokenLanguages.length() > 0) {
        for (i in 0 until spokenLanguages.length()) {
            val language = spokenLanguages.getJSONObject(i)
            if (spokenLanguagesString != "")
                spokenLanguagesString += ", "
            spokenLanguagesString += language.getString("english_name")
        }
    }

    // Creators
    val creators = results.optJSONArray("created_by")

    var creatorsString by remember {
        mutableStateOf("")
    }

    creatorsString = ""
    if (creators != null && creators.length() > 0) {
        for (i in 0 until creators.length()) {
            val creator = creators.getJSONObject(i)
            if (creatorsString != "")
                creatorsString += ", "
            creatorsString += creator.getString("name")
        }
    }

    // Production companies
    val production = results.optJSONArray("production_companies")

    var productionString by remember {
        mutableStateOf("")
    }

    productionString = ""
    if (production != null && production.length() > 0) {
        for (i in 0 until production.length()) {
            val p = production.getJSONObject(i)
            if (productionString != "")
                productionString += ", "
            productionString += p.getString("name")
        }
    }

    // Keywords
    val keywords = keywordsResponse.optJSONArray("results")

    var keywordsString by remember {
        mutableStateOf("")
    }

    keywordsString = ""
    if (keywords != null && keywords.length() > 0) {
        for (i in 0 until keywords.length()) {
            val keyword = keywords.getJSONObject(i)
            if (keywordsString != "")
                keywordsString += ", "
            keywordsString += keyword.getString("name")
        }
    }

    val res = Result(
        posterPath = posterPath,
        name = name,
        tagline = tagline,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        adult = adult,
        overview = overview,
        type = type,
        status = status,
        cast = cast,
        lastEpisodeToAir = lastEpisodeToAir,
        seasonPoster = seasonPoster,
        seasons = seasons,
        screenPercentage = screenPercentage,
        episodeRunTime = episodeRunTime as Int,
        originCountry = originCountry,
        genresString = genresString,
        spokenLanguagesString = spokenLanguagesString,
        creatorsString = creatorsString,
        productionString = productionString,
        keywordsString = keywordsString
        )

    fetchingResults = false

    DisposableEffect(Unit) {
        onDispose {
            results = JSONObject()
            keywordsResponse = JSONObject()
            castMembers = emptyList()
            seasonDetails = JSONObject()
            episodeList = emptyList()
            /*posterPath = ""
            name = ""
            tagline = ""
            releaseDate = ""
            voteAverage = 0.0
            adult = false
            overview = ""
            type = ""
            status = ""
            cast = null
            lastEpisodeToAir = null
            seasonPoster = ""
            seasons = null
            screenPercentage = 0.9f
            episodeRunTimeArray = null
            episodeRunTime = 0
            country = null
            originCountry = ""
            genresString = ""
            genres = null
            spokenLanguages = null
            spokenLanguagesString = ""
            creators = null
            creatorsString = ""
            production = null
            productionString = ""
            keywords = null
            keywordsString = ""*/

        }
    }


    if (fetchingResults) {
        LoadingScreen()
    }

    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                Column(
                    modifier = Modifier
                    //.background(Color.Cyan)
                    //.background(Color.Black)
                ) {


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        RemoteImage(

                            url = IMG_PATH_400 + res.posterPath,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .alpha(.5f),

                            //placeholderResId = R.drawable.movie_poster
                        )

                        Box(
                            modifier = Modifier
                                .height(500.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        startY = 0f,
                                        endY = Float.POSITIVE_INFINITY
                                    )
                                )
                        )


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

                                text = doubleColorText(String.format("%-22s", "Type:"), res.type)

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
                                    doubleColorText(String.format("%-20s", "Genres:"), res.genresString)

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

                                text = doubleColorText(String.format("%-21s", "Status:"), res.status)

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

                                    val episodeNumber = res.lastEpisodeToAir.optInt("episode_number", 0)
                                    val episodeString =
                                        if (episodeNumber != 0) "Ep nº$episodeNumber: " else "Last episode: "
                                    val epOverview = res.lastEpisodeToAir.optString("overview", "")
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
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    TVShowDetails(tvShowId = 1)
}