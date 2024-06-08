@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.voidchallengejetpack

import android.graphics.drawable.Drawable
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay
import java.util.UUID

@Immutable
data class Person(
    val name: String,
    val profilePath: String,
    val roles: String,
    val id: String = UUID.randomUUID().toString()
)

@Immutable
data class Episode(
    val name: String,
    val runtime: Int,
    val id: String = UUID.randomUUID().toString()
)

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
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
) {
    // State to hold the text value of the search bar
    val (text, setText) = remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd

    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color.White, focusedIndicatorColor = Color.Gray, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
            value = text,
            onValueChange = {
                setText(it)
                //onSearch(it)
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
fun RatingBar(
    maxStars: Int = 5,
    rating: Double,
    modifier: Modifier,
    string: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,

    ) {
        for (i in 1..maxStars) {
            Icon(

                imageVector = Icons.Filled.Star,
                contentDescription = null,
               // tint = if (i <= rating) colorResource(id = R.color.themeColorSec) else colorResource(id = R.color.black).copy(alpha = 0.8f),
                modifier = modifier
            )
        }

        if (string.isNotEmpty()) {
            Text(
                text = string,
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RemoteImage(url: String, modifier: Modifier = Modifier, placeholderResId: Int? = null, profile : Boolean? = false) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = modifier
        ) {
        it.error(placeholderResId)
            .load(url)

    }
    /*val painter = rememberImagePainter(
        data = url,
        builder = {
            placeholderResId?.let { placeholder(it) }
            size(500)

        })
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )*/
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
fun doubleColorText(prefix: String, suffix: String) : AnnotatedString {
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
fun myExposedDropdownMenuBox(nSeasons: Int): Int {
    val options = Array(nSeasons) { i -> "Season ${i + 1}" }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Season 1") }
    var selectedSeason by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
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
                    fontSize = 10.sp,

                    modifier = Modifier
                        .padding(start = 8.dp)

                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)

            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item,
                            fontSize = 10.sp
                        )},
                        onClick = {
                            selectedText = item
                            selectedSeason = options.indexOf(item) + 1
                            episodeList = emptyList()
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
fun CastMemberLayout(castMember: Person) {

    Column (
        modifier = Modifier
            .width(70.dp)
            .background(colorResource(id = R.color.black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        if (castMember.profilePath != "null") {
            RemoteImage( // Poster

                url = IMG_PATH_200 + castMember.profilePath,

                placeholderResId = R.drawable.anonymous_profile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.anonymous_profile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)

            )
        }
        if (castMember.name != "null") {
            Text(
                text = castMember.name,
                color = colorResource(id = R.color.white),
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Unknown",
                color = colorResource(id = R.color.white),
                fontSize = 8.sp,
            )
        }


        if (castMember.roles != "null") {
            Text(text = "as" , color = colorResource(id = R.color.grayish_text), fontSize = 5.sp, fontStyle = FontStyle.Italic)
            Text(
                text = castMember.roles,
                color = colorResource(id = R.color.white),
                fontSize = 6.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun EpisodeLayout(episode: Episode) {

    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.grayish_text))
            .width(100.dp)
            .height(40.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.themeColorPrim))
            .padding(2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            if (episode.runtime.toString() != "0") {
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

                    )
            } else {
                Text(
                    text = "Unknown",
                    color = colorResource(id = R.color.white),
                    fontSize = 8.sp,
                )
            }
        }
    }
}

@Composable
fun LastEpisodeLayout(name: String, runtime: Int, stillPath: String) {
    Box (

        modifier = Modifier
            .width(120.dp)
            .height(75.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.black))
            .padding(1.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.themeColorPrim)),
    ){
        if (stillPath != "null") {
            RemoteImage( // Poster

                url = IMG_PATH_200 + stillPath,

                placeholderResId = R.drawable.logo_transp_large,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .alpha(.5f)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.logo_transp_large),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .alpha(.4f)

            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (name != "") {
                Text(
                    text = name,
                    color = colorResource(id = R.color.themeColorSec),
                    fontSize = 10.sp,
                    fontWeight = FontWeight(1000),
                    modifier = Modifier.widthIn(max = 80.dp)
                )
            }

            if (runtime.toString() != "0") {
                Text(
                    text = "$runtime min",
                    color = colorResource(id = R.color.white),
                    fontSize = 6.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    val castMember = Person(
        name = "John Doe",
        profilePath = "https://sample-url.com/poster.jpg",
        roles = "Some Character",
    )

    val episode = Episode(
        name = "Episode 1",
        runtime = 37,
    )

    //CastMemberLayout(castMember = castMember)

    EpisodeLayout(episode = episode)

    //LastEpisodeLayout(name = "Episode 1", runtime = 60, stillPath = "https://sample-url.com/poster.jpg")

}