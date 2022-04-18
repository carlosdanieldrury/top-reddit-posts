package com.drury.topredditposts.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    //limit=30&after=t31qa3v3&count=10

    @GET("/top.json")
    suspend fun getTopReddits(
        @Query("after") after : String? = null,
//        @Query("count") count : Int = 10,
        @Query("limit") limit : Int = 10
    ) : Response<RemoteRedditPostList>
}