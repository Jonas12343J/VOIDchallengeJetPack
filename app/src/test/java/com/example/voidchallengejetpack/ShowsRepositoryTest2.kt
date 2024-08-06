package com.example.voidchallengejetpack

import com.example.voidchallengejetpack.data.remote.ShowsAPI
import com.example.voidchallengejetpack.data.remote.responses.ShowResult
import com.example.voidchallengejetpack.data.remote.responses.ShowsResultList
import com.example.voidchallengejetpack.repository.ShowRepository
import com.example.voidchallengejetpack.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoException
import java.lang.Error
import java.net.UnknownHostException

class ShowRepositoryTest {

    @Mock
    private lateinit var showsAPI: ShowsAPI

    private lateinit var showRepository: ShowRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        showRepository = ShowRepository(showsAPI)
    }

    @Test
    fun `test getPopularShows with all info on ShowResult, returns expected result with no errors`() = runBlocking {
        // Arrange
        val expectedShows = listOf(
            ShowResult(adult = false, backdrop_path = "backdrop", first_air_date = "1/2/2001", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 1", origin_country = listOf("US"), original_language = "english",  original_name = "og name", overview = "random text...", popularity = .0, poster_path = "poster", vote_average = 1.0, vote_count = 1),
            ShowResult(adult = false, backdrop_path = "backdrop", first_air_date = "1/2/2002", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 2", origin_country = listOf("US"), original_language = "english",  original_name = "og name", overview = "random text...", popularity = .0, poster_path = "poster", vote_average = 1.0, vote_count = 1),
            ShowResult(adult = false, backdrop_path = "backdrop", first_air_date = "1/2/2003", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 3", origin_country = listOf("US"), original_language = "english",  original_name = "og name", overview = "random text...", popularity = .0, poster_path = "poster", vote_average = 1.0, vote_count = 1),
            )

        val response = try {
            showsAPI.getPopularShows()
        } catch (e: Exception) {
            Error("Check your internet connection...")
        }

        `when`(response).thenReturn(ShowsResultList(1, expectedShows.sortedByDescending { it.vote_average }, 1, 1))

        // Act
        val result = showRepository.getPopularShows()

        // Assert
        assertEquals(expectedShows.sortedByDescending { it.vote_average }, result.data!!.results.sortedByDescending { it.vote_average })
    }

    @Test
    fun `test getPopularShows with missing info on ShowResults, returns result with no errors`() = runBlocking {
        // Arrange
        val expectedShows = listOf(
            // no adult AND no overview
            ShowResult(backdrop_path = "backdrop", first_air_date = "1/2/2001", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 1", origin_country = listOf("US"), original_language = "english",  original_name = "og name", popularity = .0, poster_path = "poster", vote_average = 1.0, vote_count = 1),
            // no backdrop_path AND no original_name
            ShowResult(adult = false, first_air_date = "1/2/2002", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 2", origin_country = listOf("US"), original_language = "english",  overview = "random text...", popularity = .0, poster_path = "poster", vote_average = 1.0, vote_count = 1),
            // no first_air_date AND no vote_count
            ShowResult(adult = false, backdrop_path = "backdrop", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 3", origin_country = listOf("US"), original_language = "english",  original_name = "og name", overview = "random text...", popularity = .0, poster_path = "poster", vote_average = 1.0),
            // no genre_ids AND no vote_average
            ShowResult(adult = false, backdrop_path = "backdrop", first_air_date = "1/2/2003", id =  1, name = "Show 4", origin_country = listOf("US"), original_language = "english",  original_name = "og name", overview = "random text...", popularity = .0, poster_path = "poster", vote_count = 1),
            // no origin_country AND no popularity
            ShowResult(adult = false, backdrop_path = "backdrop", first_air_date = "1/2/2004", genre_ids = listOf(1, 2, 3), id =  1, name = "Show 5", original_language = "english",  original_name = "og name", overview = "random text...", poster_path = "poster", vote_average = 1.0, vote_count = 1),
            // only name, id and poster_path
            ShowResult(id =  1, name = "Show 6", poster_path = "poster"),
        )

        val response = try {
            showsAPI.getPopularShows()
        } catch (e: Exception) {
            Error("Check your internet connection...")
        }

        `when`(response).thenReturn(ShowsResultList(1, expectedShows.sortedByDescending { it.vote_average }, 1, 1))

        // Act
        val result = showRepository.getPopularShows()

        // Assert
        assertEquals(expectedShows.sortedByDescending { it.vote_average }, result.data!!.results.sortedByDescending { it.vote_average })
    }

    @Test
    fun `test getPopularShows with empty list of ShowResults, returns empty result with no errors`() = runBlocking {
        // Arrange
        val expectedShows = emptyList<ShowResult>()

        val response = try {
            showsAPI.getPopularShows()
        } catch (e: Exception) {
            Error("Check your internet connection...")
        }

        `when`(response).thenReturn(ShowsResultList(1, emptyList(), 1, 1))

        // Act
        val result = showRepository.getPopularShows()

        // Assert
        assertEquals(expectedShows.sortedByDescending { it.vote_average }, result.data!!.results.sortedByDescending { it.vote_average })
    }

    @Test
    fun `test getPopularShows with exception, returns error`() {
        // Assert

        assertThrows(MockitoException::class.java) {
            runBlocking {

                // Arrange
                val expectedError = "Check your internet connection..."

                val response = try {
                    showsAPI.getPopularShows()
                } catch (e: UnknownHostException) {
                    Error("Check your internet connection...")
                } catch (e: Exception) {
                    Error("An unknown error occurred...")
                }

                `when`(response).thenThrow(UnknownHostException(expectedError))
                // Act
                val result = showRepository.getPopularShows()

                (result as Resource.Error).message
            }
        }
    }


}