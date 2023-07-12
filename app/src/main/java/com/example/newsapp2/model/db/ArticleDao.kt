package com.example.newsapp2.model.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp2.model.Article

@Dao
interface NewsArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(articles: Article)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<Article>

}