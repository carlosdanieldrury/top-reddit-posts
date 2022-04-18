package com.drury.topredditposts.presentation.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drury.topredditposts.domain.TopRedditUseCase
import com.drury.topredditposts.domain.model.RedditPost
import kotlinx.coroutines.launch

class MainListViewModel(
    private val topRedditUseCase: TopRedditUseCase
) : ViewModel() {

    val mutableTopPostsListLiveData = MutableLiveData<List<RedditPost>>()
    val mutableTopPostsNewListLiveData = MutableLiveData<List<RedditPost>>()
    val mutableIsLoadingMoreDataLiveData = MutableLiveData<Boolean>(false)
    val screenStateLiveData = MutableLiveData<ScreenState>()

    sealed class ScreenState {
        object Content : ScreenState()
        object Error : ScreenState()
        object Loading : ScreenState()
    }

    fun getInitialTopPosts() {
        viewModelScope.launch {
            screenStateLiveData.postValue(ScreenState.Loading)
            val result = topRedditUseCase.getTopRedditPosts()
            if (result.isSuccess) {
                mutableTopPostsListLiveData.postValue(result.getOrDefault(emptyList()))
                screenStateLiveData.postValue(ScreenState.Content)
            } else {
                screenStateLiveData.postValue(ScreenState.Error)
            }
        }
    }

    fun getMoreTopPosts(after : String) {
        viewModelScope.launch {
            mutableIsLoadingMoreDataLiveData.postValue(true)
            val result = topRedditUseCase.getTopRedditPosts(after)
            if (result.isSuccess) {
                mutableTopPostsNewListLiveData.postValue(result.getOrDefault(emptyList()))
            }
            mutableIsLoadingMoreDataLiveData.postValue(false)
        }
    }

    fun shouldLoadImage(redditPost: RedditPost) : Boolean {
        return topRedditUseCase.shouldLoadImage(redditPost)
    }
}
