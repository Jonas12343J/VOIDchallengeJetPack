@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.voidchallengejetpack

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.voidchallengejetpack.data.remote.responses.CastMember
import com.example.voidchallengejetpack.data.remote.responses.SeasonDetails
import com.example.voidchallengejetpack.util.Constants.IMG_PATH_200
import kotlinx.coroutines.delay
import com.example.voidchallengejetpack.data.remote.responses.Episode
import com.example.voidchallengejetpack.showslist.ShowListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SkeletonWrapperView(
    modifier: Modifier = Modifier,
    contentView: @Composable () -> Unit,
    defaultPrototypeView: (@Composable () -> Unit)? = null,
    shape: Shape = RectangleShape,
    loading : Boolean

) {
    val defaultSize = remember {
        mutableStateOf(IntSize.Zero)
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(shape)
            .onSizeChanged { defaultSize.value = it },
        contentAlignment = Alignment.Center
    ) {

        contentView()

        if (loading) {
            defaultPrototypeView?.let { it() }
        }
    }

    if (loading) {

        val animateColor = remember { Animatable(Color.Gray) }

        val animationToggle = remember {
            mutableStateOf(false)
        }

        // if loading show skeleton with animation

        if (animationToggle.value) {
            LaunchedEffect (key1 = null, block = {
                animateColor.animateTo(
                    Color.Gray,
                    animationSpec = tween(400)
                )

                animationToggle.value = false
            })
        }
        else {
            LaunchedEffect (key1 = null, block = {
                animateColor.animateTo(
                    Color.LightGray,
                    animationSpec = tween(400)
                )

                animationToggle.value = true
            })
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(animateColor.value)
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(
                            width = defaultSize.value.width.toDp(),
                            height = defaultSize.value.height.toDp()
                        )
                    }
                )
        )
    }
}

@Composable
@Stable
fun RatingBar(
    maxStars: Int = 5,
    rating: Double,
    modifier: Modifier,
    string: String? = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,

        ) {
        for (i in 1..maxStars) {
            Icon(

                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= rating) colorResource(id = R.color.themeColorSec) else colorResource(id = R.color.grayish_text).copy(alpha = 0.8f),
                modifier = modifier
            )
        }

        if (string!!.isNotEmpty()) {
            Text(
                text = string,
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    val coroutineScope = rememberCoroutineScope()
    var loadingText by remember { mutableStateOf("Fetching results") }

    LaunchedEffect(key1 = coroutineScope) {
        while (true) {
            delay(1000L) // delay for 1 second
            when (loadingText) {
                "Fetching results" -> loadingText = "Fetching results."
                "Fetching results." -> loadingText = "Fetching results.."
                "Fetching results.." -> loadingText = "Fetching results..."
                "Fetching results..." -> loadingText = "Fetching results"
            }
        }
    }
    Column (
        Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        Text(
            text = loadingText,
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressIndicator(color = colorResource(id = R.color.themeColorSec))

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    viewModel: ShowListViewModel = hiltViewModel()
) {
    // State to hold the text value of the search bar
    val (text, setText) = remember { mutableStateOf(viewModel.searchText.value) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd

    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = Color.Gray, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
            value = text,
            onValueChange = {
                setText(it)
            },
            placeholder = { Text(text = "What are you looking for?") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch(text)
                }
            ),

            modifier = Modifier.fillMaxWidth(),
        )

        if (text.isNotEmpty()) {
            // Clear button to clear the search text
            IconButton(
                onClick = { setText("") },
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = colorResource(id = R.color.themeColorSec)
                )
            }
        }
    }
}

@Composable
fun doubleColorText(
    prefix: String,
    suffix: String
) : AnnotatedString {
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(colorResource(id = R.color.grayish_text))) {
            append(prefix)
        }
        withStyle(style = SpanStyle(color = Color.White)) {
            append(suffix)
        }
    }
    return text
}

@Composable
fun myExposedDropdownMenuBox(
    itemList: List<SeasonDetails>
): Int {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(itemList[0].name) }
    var selectedSeason by remember { mutableIntStateOf(itemList[0].season_number) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(true) {
                detectTapGestures(
                    onTap = {
                        expanded = !expanded
                    }
                )
            }
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }

        ) {
            Row (
                modifier = Modifier
                    .menuAnchor()
                    .background(color = colorResource(id = R.color.grayish_text)),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(

                    text = selectedText,

                    modifier = Modifier
                        .padding(10.dp)

                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item.name,
                        )},
                        onClick = {
                            selectedText = item.name
                            selectedSeason = item.season_number
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    return selectedSeason
}

@Composable
fun CastMemberLayout(
    castMember: CastMember
) {
    Column (
        modifier = Modifier
            .width(70.dp)
            .background(colorResource(id = R.color.black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if (castMember.profile_path != "") {
            SubcomposeAsyncImage(
                model = IMG_PATH_200 + castMember.profile_path,
                filterQuality = FilterQuality.Low,
                contentDescription = null,

                loading = {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.themeColorSec),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .scale(.5f)
                    )
                },

                error = {
                    Image(
                        painter = painterResource(id = R.drawable.anonymous_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                },

                )
        } else  {
            Image(
                painter = painterResource(id = R.drawable.anonymous_profile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }

        if (castMember.name != "") {
            Text(
                text = castMember.name,
                color = colorResource(id = R.color.white),
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Anonymous",
                color = colorResource(id = R.color.white),
                fontSize = 8.sp,
            )
        }

        if (castMember.roles.isNotEmpty() && castMember.roles[0].character != "") {
            Text(text = "as" , color = colorResource(id = R.color.grayish_text), fontSize = 5.sp, fontStyle = FontStyle.Italic)
            Text(
                text = castMember.roles[0].character,
                color = colorResource(id = R.color.white),
                fontSize = 6.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun EpisodeLayout(
    episode: Episode,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(100.dp)
            .height(40.dp)
            .padding(.5f.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(colorResource(id = R.color.themeColorPrim)),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(.1f)
                .fillMaxHeight()
                .background(colorResource(id = R.color.grayish_text))
        ) {}

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (episode.runtime != 0) {
                Text(
                    text = episode.runtime.toString() + " minutes",
                    color = colorResource(id = R.color.white),
                    fontSize = 6.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (episode.name != "") {
                Text(
                    text = episode.name,
                    color = colorResource(id = R.color.white),
                    fontSize = 8.sp,
                    fontWeight = FontWeight(750),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    )
            } else {
                Text(
                    text = "Unknown",
                    color = colorResource(id = R.color.white),
                    fontSize = 8.sp,
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth(.1f)
                .fillMaxHeight()
        ) {}

    }
}

@Composable
fun NextEpisodeLayout(
    episode: Episode,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.themeColorPrim)),
        verticalAlignment = Alignment.Top,
    ){
        Box (
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = R.color.themeColorPrim)),

        ) {
            if (episode.still_path != "") {
                SubcomposeAsyncImage(
                    model = IMG_PATH_200 + episode.still_path,
                    filterQuality = FilterQuality.Low,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .alpha(.5f),

                    loading = {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.themeColorSec),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .scale(.5f)
                        )
                    },

                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_transp_large),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    },

                    )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo_transp_large),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(.4f)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            if (episode.season_number != 0) {
                Text(
                    text = "Season ${episode.season_number}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(1000),
                    textAlign = TextAlign.Center,
                )
            }
            if (episode.name != "") {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = episode.name,
                    color = colorResource(id = R.color.themeColorSec),
                    fontSize = 12.sp,
                    fontWeight = FontWeight(500),
                )
            }

            if (episode.air_date != "") {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Coming out in " + SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(
                        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(episode.air_date)!!),
                    color = colorResource(id = R.color.white),
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                )
            }

            if (episode.runtime.toString() != "0") {
                Spacer(Modifier.height(4.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Image(
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = null,
                        Modifier
                            .size(12.dp)
                            .padding(end = 4.dp)
                    )

                    Text(
                        text = "${episode.runtime} min",
                        color = colorResource(id = R.color.white),
                        fontSize = 6.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                    )
                }
            }

        }

    }
}