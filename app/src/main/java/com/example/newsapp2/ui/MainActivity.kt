package com.example.newsapp2.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val titles =
        arrayListOf("Home", "Sports", "Entertainment", "Business", "Health", "Science")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val x = binding.root
        tabLayout = binding.tlNewsTitles
        setContentView(x)
        viewPager2 = binding.viewPager

        viewPagerAdapter = ViewPagerAdapter(application, this)
        viewPager2.adapter = viewPagerAdapter
        viewPager2.offscreenPageLimit = 1
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()

        if (savedInstanceState == null) {
            viewPager2.post {
                viewPager2.setCurrentItem(0, false)
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewPager2.post {
            viewPager2.setCurrentItem(0, false)
        }
    }


}

// this is the reference code


//        val fragmentManager = supportFragmentManager
//        val fragmentDatabase1 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//         val fragmentDatabase2 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//         val fragmentDatabase3 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//         val fragmentDatabase4 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//         val fragmentDatabase5 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//         val fragmentDatabase6 = Room.inMemoryDatabaseBuilder(
//            application,
//            NewsDatabase::class.java
//        ).build()
//            val fragmentDao1 = fragmentDatabase1.getArticleDao()
//            val newsRepository1 = NewsRepository("general", fragmentDao1)
//
//            val fragmentDao2 = fragmentDatabase2.getArticleDao()
//            val newsRepository2 = NewsRepository("sports", fragmentDao2)
//
//            val fragmentDao3 = fragmentDatabase3.getArticleDao()
//            val newsRepository3 = NewsRepository("entertainment", fragmentDao3)
//
//            val fragmentDao4 = fragmentDatabase4.getArticleDao()
//            val newsRepository4 = NewsRepository("business", fragmentDao4)
//
//
//            val fragmentDao5 = fragmentDatabase5.getArticleDao()
//            val newsRepository5 = NewsRepository("health", fragmentDao5)
//
//            val fragmentDao6 = fragmentDatabase6.getArticleDao()
//            val newsRepository6 = NewsRepository("science", fragmentDao6)
//
//        val fragments = mutableListOf<Fragment> (
//            MainFragment(application, "general", newsRepository1),
//            MainFragment(application, "sports", newsRepository2),
//            MainFragment(application, "entertainment", newsRepository3),
//            MainFragment(application, "business", newsRepository4),
//            MainFragment(application, "health", newsRepository5),
//            MainFragment(application, "science", newsRepository6)
//        )
