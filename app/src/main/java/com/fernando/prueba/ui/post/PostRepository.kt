package com.fernando.prueba.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fernando.prueba.data.AppDatabase
import com.fernando.prueba.models.Post
import com.fernando.prueba.network.ApiService
import com.fernando.prueba.network.SafeApiRequest
import com.fernando.prueba.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.bindings.WithContext

class PostRepository(
    private val apiService: ApiService,
    private val db:AppDatabase
) :SafeApiRequest() {


    internal suspend fun fetchPosts() = apiRequest { apiService.getPosts() }

    internal suspend fun deletePost(id:String) = apiRequest { apiService.deletePost(id) }

    suspend fun savePosts(list: List<Post>) {
        db.dao().saveAllPosts(list);
    }

    suspend fun readPost(isRead:Boolean, id:Int) = db.dao().readPost(isRead, id);

    fun getPosts() = db.dao().getPosts()

    fun getAllPosts()= db.dao().getAllPosts();

    fun getFavorites() = db.dao().getFavoritePosts(true)

    suspend fun deletePost(id: Int) = db.dao().deletePost(id)

    suspend fun deleteAllPosts() = db.dao().deleteAllPosts()



}