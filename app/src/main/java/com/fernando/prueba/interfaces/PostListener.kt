package com.fernando.prueba.interfaces

import com.fernando.prueba.models.Post

interface PostListener {
    fun onSelectedPost(post: Post, position:Int)
}