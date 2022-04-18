package com.drury.topredditposts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RedditPostParent(
    val kind: String,
    val data: RedditPostData
)

data class RedditPostData(
    val children: List<RedditPost>,
    val after: String?,
    val before: String?
)

@Parcelize
data class RedditPost(
    val thumbnail: String?,
    val url: String?,
    val title: String?,
    val author: String?,
    val name: String?
) : Parcelable
