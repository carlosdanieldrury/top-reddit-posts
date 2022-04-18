package com.drury.topredditposts.data.network

import com.google.gson.annotations.SerializedName

data class RemoteRedditPostList(
    val kind: String,
    val data: RemoteRedditPostData
)

data class RemoteRedditPostData(
    val children: List<RemoteRedditPostDataChild>,
    val after: String?,
    val before: String?
)

data class RemoteRedditPostDataChild(
    val kind: String,
    val data: RemoteRedditPost
)

data class RemoteRedditPost(
    val thumbnail: String?,
    val url: String?,
    val title: String?,
    val author: String?,
    val name: String?
)