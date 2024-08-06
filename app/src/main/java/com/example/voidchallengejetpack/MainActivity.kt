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

var displayedText by mutableStateOf("Popular right now:")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
