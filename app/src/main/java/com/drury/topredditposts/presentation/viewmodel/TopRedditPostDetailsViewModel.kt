package com.drury.topredditposts.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.drury.topredditposts.domain.TopRedditUseCase
import com.drury.topredditposts.domain.model.RedditPost

class TopRedditPostDetailsViewModel(
    private val topRedditUseCase: TopRedditUseCase
) : ViewModel() {

    fun shouldLoadImage(redditPost: RedditPost) : Boolean {
        return topRedditUseCase.shouldLoadImage(redditPost)
    }

}