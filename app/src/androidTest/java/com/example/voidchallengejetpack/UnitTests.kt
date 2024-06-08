package com.example.voidchallengejetpack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import org.json.JSONObject

import org.junit.Test

import org.junit.Assert.*

class UnitTests {
    @Test
    fun TestEmptyOverviewDetailPage() {
        // Context of the app under test.
        // ID = 209374 -> no overview
        // ID = 210479 -> has overview
        val tvShowID = 209374
        var tvShow by mutableStateOf(JSONObject())
        getShowDetailsAsync(
            scope = null,
            showID = tvShowID,
            onSuccess = { response ->
                //tvShow = setShowDetails(response)
                tvShow = JSONObject(response)
            },
            onFailure = { exception ->
                // Handle network request failure
                println("Error: ${exception.message}")
            }
        )

        if (!tvShow.has("overview")) {
            assertEquals("", tvShow.getString("overview"))
        }
        else {
            val overview = tvShow.getString("overview")
            assertNotEquals("", overview)
        }


        //TVShowDetails(tvShowId = tvShowID)
        //assertEquals("com.example.voidchallengejetpack", appContext.packageName)
    }
}