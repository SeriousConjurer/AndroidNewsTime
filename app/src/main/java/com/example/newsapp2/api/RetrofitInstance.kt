package com.example.newsapp2.api

import com.example.newsapp2.utils.constant.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}


// this is the reference code

//class RetrofitInstance {
//    companion object{
//
//        val interceptor = HttpLoggingInterceptor().apply {
//            this.level = HttpLoggingInterceptor.Level.BODY
//        }
//        val client = OkHttpClient.Builder().apply {
//            this.addInterceptor(interceptor)
//        }.build()
//        fun getRetrofitInstance(): Retrofit {
//            return Retrofit.Builder().
//            baseUrl(BASE_URL).
//            client(client).
//            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).
//            build()
//        }
//    }
//}
