package com.example.newsapp2.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.newsapp2.R
import com.example.newsapp2.model.Resource
import com.example.newsapp2.model.Article
import com.example.newsapp2.model.NewsResponse
import com.example.newsapp2.model.repository.NewsRepository
import internetConnection

import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModelProviderFactory(
    val app: Application,
    private val newsRepository: NewsRepository,
    private val category: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app, newsRepository, category) as T
    }
}


class MainViewModel(
    private val app: Application,
    private val newsRepository: NewsRepository,
    private val category: String,
) : AndroidViewModel(app) {

    private val tag = this::class.java.name

    val mainNews: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
    var mainNewsPage = 1
    var mainNewsResponse: List<Article>? = null

    init {
        Log.d("tag", "viewModel created" + category)
        getCategoryNews()
    }

    fun getCategoryNews() = viewModelScope.launch {
        Log.d("abc", "in the getCategory" + category)
        safeMainNews()
    }


    private suspend fun handleCategoryNewsResponse(response: Response<NewsResponse>): Resource<List<Article>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                mainNewsPage++
                Log.d(
                    "abc",
                    resultResponse.articles.size.toString() + " adding data to the database"
                )
                for (article in resultResponse.articles) {
                    newsRepository.upsert(article)

                }

                Log.d("abc", "in the handleCategory" + category)
                return getNewsFromDatabase()
                //return Resource.Success(mainNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private suspend fun getNewsFromDatabase(): Resource<List<Article>> {
        try {
//            Log.d("abc","in the getNewsFromDatabase" + category)
            Log.d("tag","data from dataabse " + category)
            val data = newsRepository.getNewsFromDatabase()
//            Log.d("databasease fetching", category)
            mainNewsResponse = data
            return Resource.Success(data)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString(), null)
        }
    }

    fun fetchNewsFromAPI() = viewModelScope.launch {
        try {
            if (internetConnection()) {
                Log.d("tag","fetch Newsfrom api " + category)
                val response = newsRepository.getCategoryNews(category, mainNewsPage)
                mainNews.postValue(handleCategoryNewsResponse(response))


            } else {
                mainNews.postValue(Resource.Error("no_internet", null))
            }
        } catch (t: Throwable) {
            mainNews.postValue(
                Resource.Error(
                    app.getString(R.string.network_connection_failure),
                    null
                )
            )

        }
    }

    private suspend fun safeMainNews() {
        mainNews.postValue(Resource.Loading())

        Log.d("abc", mainNews.value?.data?.size.toString() + " " + category)
        val data = newsRepository.getNewsFromDatabase()
        if (data.isEmpty()) {
            fetchNewsFromAPI()
        } else {

            mainNewsResponse = data
            mainNews.postValue(Resource.Success(data))
            Log.d("abc","news posted to the diffutil" + category)
        }
//        Log.d("abc", data.size.toString() + " " + category)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("tag", category + " model destroyed")
    }


    //// convert to extension function (Kotlin)


}


// this is the reference code


//    private val _status = LiveData<String>()
//    var NewsResponseData : LiveData<NewsResponse> = LiveData()
//    val status: LiveData<String> = _status
//
//    fun getCategoryNews(category : String) : LiveData<NewsResponse> {
//        Log.d("abcd" , "It is inside the getEntertainmentNews")
//        viewModelScope.launch {
//            try {
//                /// have repo
//                /// pass retrofit instance as constu para ////
//                //// di -> pass  ////
//                /// viewmodel factory
//                /// optmisation
//                val liveResult = RetrofitInstance.getRetrofitInstance().create(NewsAPI::class.java).getNewsByCategory(category)
//                _status.value = "Success"
//                Log.d("before" , "data fetched")
//                NewsResponseData.value = liveResult.body()
//                Log.d("after","newsresponsedata updated")
//
//            }catch (e : Exception){
//                _status.value = "Failure ${e.message}"
//            }
//
//        }
//        return NewsResponseData
//    }
