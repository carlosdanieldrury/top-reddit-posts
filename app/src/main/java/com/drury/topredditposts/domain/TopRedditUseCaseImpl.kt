package com.drury.topredditposts.domain

import com.drury.topredditposts.domain.model.RedditPost
import com.drury.topredditposts.domain.model.RedditPostParent

class TopRedditUseCaseImpl(
    private val redditRepository: TopRedditRepository
) : TopRedditUseCase {

    override suspend fun getTopRedditPosts(after : String?): Result<List<RedditPost>> {
        return redditRepository.getTopRedditPosts(after)
    }

    override fun shouldLoadImage(redditPost: RedditPost) : Boolean = redditPost.thumbnail?.contains("\\w+[.][jpg|png]".toRegex()) == true

}
