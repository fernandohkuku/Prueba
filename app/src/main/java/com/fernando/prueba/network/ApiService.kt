package com.fernando.prueba.network

import com.fernando.prueba.interceptors.NetworkInterceptorConnection
import com.fernando.prueba.models.Post
import com.fernando.prueba.models.User
import com.fernando.prueba.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("/posts")
    suspend fun getPosts():Response<List<Post>>

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id:String):Response<User>


    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: String):Response<Post>



    companion object{
        operator fun invoke(
            networkInterceptorConnection: NetworkInterceptorConnection
        ):ApiService{
            val client = OkHttpClient()
                .newBuilder().readTimeout(40, TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .addInterceptor(networkInterceptorConnection)
                .build()

            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiService::class.java)
        }
    }

}