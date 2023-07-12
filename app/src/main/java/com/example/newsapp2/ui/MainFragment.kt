package com.example.newsapp2.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.R
import com.example.newsapp2.model.Resource
import com.example.newsapp2.databinding.FragmentMainBinding
import com.example.newsapp2.ui.recyclerView.RecyclerViewAdapter
import com.example.newsapp2.model.repository.NewsRepository
import com.example.newsapp2.utils.constant.Companion.QUERY_ITEM_SIZE
import com.example.newsapp2.viewModels.MainViewModel
import com.example.newsapp2.viewModels.NewsViewModelProviderFactory
import internetConnection


class MainFragment(
    private val app: Application,
    private val category: String,
    private val newsRepository: NewsRepository
) : Fragment() {

    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    lateinit var newsAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAG", category + " created")
        mainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mainBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


// ***************** setting the view model and factory **************************************************************************

        val newsViewModelProviderFactory =
            NewsViewModelProviderFactory(app, newsRepository, category)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory)[MainViewModel::class.java]

//  swipe refresh ********************************************************************************************************************************

        mainBinding.swipeRefreshLayout.setOnRefreshListener {
            if (viewModel.mainNewsResponse == null) {
//                Log.d("tag" , "swipe refresh")
                viewModel.getCategoryNews()
            } else if (viewModel.internetConnection())
                Toast.makeText(activity, R.string.stable_connection, Toast.LENGTH_LONG).show()
            else
                Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show()
            mainBinding.swipeRefreshLayout.isRefreshing = false
        }
//  recyclerView with diffUtil ********************************************************************************************************************************

        newsAdapter = RecyclerViewAdapter(category)
        mainBinding.rvNewsCard.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
        viewModel.mainNews.observe(viewLifecycleOwner, Observer { response ->
//            Log.d("abc", "It is inside the viewModel oberver of " + category)
            when (response) {
                is Resource.Success -> {
//                    Log.d("abc", "It is inside the viewModel oberver SUCCESS of " + category)

                    hideProgressBar()
                    mainBinding.ivInternet.visibility = View.INVISIBLE

// ***************** adding changes to diffUtil and adding pagination **************************************************************************

                    response.data.let { data ->
                        Log.d("abc", category + " data")
                        mainBinding.rvNewsCard.addOnScrollListener(this@MainFragment.scrollerListener)
                        if (data != null) {

//                            Log.d("abc" , data.size.toString() + " inside the data not null " + category)
                            newsAdapter.differ.submitList(data.toList())
//                            Log.d("abc" , data.size.toString() + " below differ.submitList() " + category)

//                            Log.d("TAG",category+" data should be loaded")

                            val totalPages = data.size / QUERY_ITEM_SIZE + 2
                            isLastPage = viewModel.mainNewsPage == totalPages
                        }

                    }
                    isLoading = false
                }

                is Resource.Error -> {
//                    Log.d("abc", "It is inside the viewModel oberver  ERROR of " + category)

                    hideProgressBar()
                    // Log.d("look for internet connectivity error" , viewModel.mainNews.value?.data?.articles?.size.toString())
                    if (viewModel.mainNewsResponse == null)
                        mainBinding.ivInternet.visibility = View.VISIBLE
                    isLoading = false
                    response.message.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
//                    Log.d("abc", "It is inside the viewModel oberver LOADING of " + category)

                    mainBinding.ivInternet.visibility = View.INVISIBLE
                    showProgressBar()
                    isLoading = true
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("abc", "$category destroyed")
    }

    // Pagination part *****************************************************************************************************************
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollerListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_ITEM_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastPage && isNotBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                if (viewModel.internetConnection()) {
                    Log.d("TAG", "fetching data")
                    viewModel.fetchNewsFromAPI()
                } else {
                    Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT).show()
                }
                //Log.d("LoadingNewPage","LoadingNewPage for $category")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }

    // progress bar   *****************************************************************************************************************
    private fun hideProgressBar() {
        isLoading = false
        mainBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        isLoading = true
        mainBinding.paginationProgressBar.visibility = View.VISIBLE
        if (isLastPage) {
            mainBinding.rvNewsCard.setPadding(0, 0, 0, 0)
        }
    }

}


// this is the reference code


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        mainBinding = FragmentMainBinding.inflate(inflater,container,false)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel.getCategoryNews(category)
//
//        viewModel.NewsResponseData.observe(viewLifecycleOwner, Observer { data ->
//            // Update your UI with the fetched data
//            //view?.findViewById<TextView>(R.id.textView)?.text = data.articles[0].title.toString()
//            val recyclerView = mainBinding.recyclerViewMain
//            recyclerView.layoutManager = LinearLayoutManager(activity)
//            recyclerView.adapter = RecyclerViewAdapter(data.articles)
//        })
//
//        return mainBinding.root
//    }
