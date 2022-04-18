package com.drury.topredditposts.data.network

import com.drury.topredditposts.domain.TopRedditRepository
import com.drury.topredditposts.domain.model.RedditPost
import com.drury.topredditposts.domain.model.RedditPostParent
import com.drury.topredditposts.domain.model.toRedditPostParent

class TopRedditRepositoryImpl(
    private val redditApi: RedditApi
) : TopRedditRepository {

    override suspend fun getTopRedditPosts(after : String?) : Result<List<RedditPost>> {
        return try {
            val response = redditApi.getTopReddits(after = after)
            Result.success(response.body()!!.toRedditPostParent().data.children)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
