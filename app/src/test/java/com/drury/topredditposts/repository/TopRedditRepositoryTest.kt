package com.drury.topredditposts.repository

import com.drury.topredditposts.data.network.*
import com.drury.topredditposts.domain.TopRedditRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.koin.test.KoinTest
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(JUnit4::class)
class TopRedditRepositoryTest : KoinTest {

    private val r4 = RemoteRedditPost(
        thumbnail = "https://b.thumbs.redditmedia.com/SIgWxEMlvRWE530gJwtMZlUaA14MHGKfAa7gKwseF7o.jpg",
        url = "url",
        title = "title",
        author = "author",
        name = "Name"
    )

    private val r3 = RemoteRedditPostDataChild(
        kind = "test",
        data = r4
    )

    private val r2 = RemoteRedditPostData(
        children = listOf(r3),
        after = "after",
        before = "before"
    )

   private val r1 = RemoteRedditPostList(
       kind = "test",
       data = r2
   )

    val topRedditRepository: TopRedditRepository by inject()
    val api : RedditApi by inject()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        startKoin {
            modules(
                module {
                    single<RedditApi> { mockk<RedditApi>() }
                    single<TopRedditRepository> { TopRedditRepositoryImpl(get()) }
                })
        }
        coEvery { api.getTopReddits() } returns Response.success(r1)
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `should inject my components`() {
        // directly request an instance
        val repository = get<TopRedditRepository>()

        assertNotNull(repository)
        assertEquals(repository, topRedditRepository)
    }

    @Test
    fun test_get_top_reddit_posts_retrieves_data() {
        runBlocking {
            val data = topRedditRepository.getTopRedditPosts()

            assertNotNull(data)
        }
    }

    @Test
    fun `test get top reddit posts retrives a post with thumbnail`() {
        runBlocking {
            val data = topRedditRepository.getTopRedditPosts()

            assertEquals("https://b.thumbs.redditmedia.com/SIgWxEMlvRWE530gJwtMZlUaA14MHGKfAa7gKwseF7o.jpg", data.getOrNull()?.firstOrNull()?.thumbnail)
        }
    }


}