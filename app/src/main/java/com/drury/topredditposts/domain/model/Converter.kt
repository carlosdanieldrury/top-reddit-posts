package com.drury.topredditposts.domain.model

import com.drury.topredditposts.data.network.RemoteRedditPost
import com.drury.topredditposts.data.network.RemoteRedditPostData
import com.drury.topredditposts.data.network.RemoteRedditPostList

fun RemoteRedditPostList.toRedditPostParent() = RedditPostParent(
    data = data.toRedditPostData(),
    kind = kind
)

fun RemoteRedditPostData.toRedditPostData() = RedditPostData(
    children = children.map { it.data.toRedditPost() },
    after = after,
    before = before
)

fun RemoteRedditPost.toRedditPost() = RedditPost(
    thumbnail = thumbnail,
    url = url,
    title = title,
    author = author,
    name = name
)