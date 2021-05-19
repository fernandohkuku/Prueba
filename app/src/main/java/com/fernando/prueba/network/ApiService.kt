package com.fernando.prueba.network

import com.fernando.prueba.models.Post
import com.fernando.prueba.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("/posts")
    suspend fun getPosts():Response<List<Post>>



    companion object{
        operator fun invoke():ApiService{
            val client = OkHttpClient()
                .newBuilder().readTimeout(40, TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiService::class.java)
        }
    }

}