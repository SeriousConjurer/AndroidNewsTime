package com.example.newsapp2.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp2.model.Article


@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getArticleDao(): NewsArticleDao

    companion object {
        @Volatile
        private var instances: MutableList<NewsDatabase> = mutableListOf()

        fun getInstance(context: Context, instanceIndex: Int): NewsDatabase {
            synchronized(this) {
                if (instanceIndex < instances.size) {
                    return instances[instanceIndex]
                }

                val instance = Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java
                ).build()

                instances.add(instance)
                return instance
            }
        }
    }

}