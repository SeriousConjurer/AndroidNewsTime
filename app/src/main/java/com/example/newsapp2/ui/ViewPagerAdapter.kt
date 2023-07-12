package com.example.newsapp2.ui

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp2.model.db.NewsDatabase
import com.example.newsapp2.model.repository.NewsRepository

class ViewPagerAdapter(val app: Application, fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val titles =
        arrayListOf<String>("Home", "Sports", "Entertainment", " Business", "Health", "Science")

    override fun getItemCount(): Int {
        return titles.size
    }


    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {

                val newsRepository =
                    NewsRepository("general", NewsDatabase.getInstance(app, 0).getArticleDao())
                MainFragment(app, "general", newsRepository)
            }

            1 -> {

                val newsRepository =
                    NewsRepository("sports", NewsDatabase.getInstance(app, 1).getArticleDao())
                MainFragment(app, "sports", newsRepository)
            }

            2 -> {

                val newsRepository = NewsRepository(
                    "entertainment",
                    NewsDatabase.getInstance(app, 2).getArticleDao()
                )
                MainFragment(app, "entertainment", newsRepository)
            }

            3 -> {

                val newsRepository =
                    NewsRepository("business", NewsDatabase.getInstance(app, 3).getArticleDao())
                MainFragment(app, "business", newsRepository)
            }

            4 -> {

                val newsRepository =
                    NewsRepository("health", NewsDatabase.getInstance(app, 4).getArticleDao())
                MainFragment(app, "health", newsRepository)
            }

            5 -> {

                val newsRepository =
                    NewsRepository("science", NewsDatabase.getInstance(app, 5).getArticleDao())
                MainFragment(app, "science", newsRepository)
            }

            else -> {

                val newsRepository =
                    NewsRepository("general", NewsDatabase.getInstance(app, 1).getArticleDao())
                MainFragment(app, "general", newsRepository)
            }
        }
    }
}