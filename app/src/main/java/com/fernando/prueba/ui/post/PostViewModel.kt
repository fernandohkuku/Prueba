package com.fernando.prueba.ui.post

import androidx.lifecycle.ViewModel
import com.fernando.prueba.models.Post

class PostViewModel(
    private val repository: PostRepository
):ViewModel() {

    suspend fun fetchPosts()= repository.fetchPosts()

    suspend fun savePosts(posts: List<Post>) = repository.savePosts(posts)

    suspend fun readPost(isRead:Boolean, id:Int) = repository.readPost(isRead, id)

    fun getPosts() = repository.getPosts()

    fun getAllPosts() = repository.getAllPosts()

    fun getPostFavorites() = repository.getFavorites()

    suspend fun deletePost(id:String) = repository.deletePost(id);

     suspend fun deletePost(id: Int)  = repository.deletePost(id)

    suspend fun deleteAllPosts() = repository.deleteAllPosts()

}