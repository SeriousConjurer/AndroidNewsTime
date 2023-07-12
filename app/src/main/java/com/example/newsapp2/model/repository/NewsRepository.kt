package com.example.newsapp2.model.repository

import com.example.newsapp2.api.RetrofitInstance
import com.example.newsapp2.model.Article
import com.example.newsapp2.model.db.NewsArticleDao

class NewsRepository(
    private val fragmentTag: String,
    private val fragmentDao: NewsArticleDao
) {
    suspend fun getCategoryNews(category: String, pageNumber: Int) =
        RetrofitInstance.api.getNewsByCategory(category, pageNumber)

    suspend fun upsert(articles: Article) = fragmentDao.upsert(articles)
    suspend fun getNewsFromDatabase() = fragmentDao.getAllArticles()
}