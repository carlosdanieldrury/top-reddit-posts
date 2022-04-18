package com.drury.topredditposts.usecase

import com.drury.topredditposts.data.network.*
import com.drury.topredditposts.domain.TopRedditRepository
import com.drury.topredditposts.domain.TopRedditUseCase
import com.drury.topredditposts.domain.TopRedditUseCaseImpl
import com.drury.topredditposts.domain.model.toRedditPost
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TopRedditUseCaseTest : KoinTest {

    private val r4 = RemoteRedditPost(
        thumbnail = "https://b.thumbs.redditmedia.com/SIgWxEMlvRWE530gJwtMZlUaA14MHGKfAa7gKwseF7o.jpg",
        url = "url",
        title = "title",
        author = "author",
        name = "Name"
    )

    private val r4Fail = RemoteRedditPost(
        thumbnail = "https://b.thumbs.redditmedia.com/SIgWxEMlvRWE530gJwtMZlUaA14MHGKfAa7gKwseF7o.xml",
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

    val topRedditUseCase: TopRedditUseCase by inject()
    val topRedditRepository: TopRedditRepository by inject()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        startKoin {
            modules(
                module {
                    single<TopRedditRepository> { mockk<TopRedditRepository>() }
                    single<TopRedditUseCase> { TopRedditUseCaseImpl(get()) }
                })
        }
        coEvery { topRedditRepository.getTopRedditPosts() } returns Result.success(r2.children.map { it.data.toRedditPost() })
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `should inject my components`() {
        // directly request an instance
        val usecase = get<TopRedditUseCase>()

        assertNotNull(usecase)
        assertEquals(usecase, topRedditUseCase)
    }

    @Test
    fun test_get_top_reddit_posts_retrieves_data() {
        runBlocking {
            val data = topRedditUseCase.getTopRedditPosts()
            assertNotNull(data)
        }
    }

    @Test
    fun `test get top reddit posts retrives a post with thumbnail`() {
        runBlocking {
            val data = topRedditUseCase.getTopRedditPosts()
            assertEquals("https://b.thumbs.redditmedia.com/SIgWxEMlvRWE530gJwtMZlUaA14MHGKfAa7gKwseF7o.jpg", data.getOrNull()?.firstOrNull()?.thumbnail)
        }
    }

    @Test
    fun `test should load image retrieves result true`() {
        val redditPost = r4.toRedditPost()
        val result = topRedditUseCase.shouldLoadImage(redditPost)
        assertTrue(result)
    }

    @Test
    fun `test should load image retrieves result false`() {
        val redditPost = r4Fail.toRedditPost()
        val result = topRedditUseCase.shouldLoadImage(redditPost)
        assertFalse(result)
    }

}