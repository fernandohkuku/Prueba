package com.fernando.prueba.interfaces

import com.fernando.prueba.models.Post
import kotlinx.coroutines.Job

interface PostListener {
    fun onSelectedPost(post: Post, position:Int)
    fun onDeletePost(post: Post, position: Int):Job
}