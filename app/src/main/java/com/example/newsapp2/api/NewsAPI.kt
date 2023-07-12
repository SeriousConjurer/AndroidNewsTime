package com.example.newsapp2.api

import com.example.newsapp2.model.NewsResponse
import com.example.newsapp2.utils.constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String = "Home",
        @Query("page") pageNumber: Int = 1,
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = API_KEY /// build-config
    ): Response<NewsResponse>
}