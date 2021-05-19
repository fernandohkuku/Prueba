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

    private val posts = MutableLiveData<List<Post>>()

    internal suspend fun fetchPosts() = apiRequest { apiService.getPosts() }

    suspend fun savePosts(list: List<Post>) {
        db.dao().saveAllPosts(list);
    }

    suspend fun readPost(isRead:Boolean, id:Int) = db.dao().readPost(isRead, id);

    fun getPosts() = db.dao().getPosts()




}