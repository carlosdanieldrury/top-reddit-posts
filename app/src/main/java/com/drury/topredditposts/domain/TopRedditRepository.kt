package com.drury.topredditposts.domain

import com.drury.topredditposts.domain.model.RedditPost
import com.drury.topredditposts.domain.model.RedditPostParent

interface TopRedditRepository {
    suspend fun getTopRedditPosts(
        after : String? = null
    ) : Result<List<RedditPost>>
}