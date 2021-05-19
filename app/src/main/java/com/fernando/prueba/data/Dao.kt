package com.fernando.prueba.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fernando.prueba.models.Post


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllPosts(posts:List<Post>)

    @Query("SELECT * FROM post")
    fun getPosts():LiveData<List<Post>>

    @Query("UPDATE post set isRead=:isRead where id=:id")
    suspend fun readPost(isRead:Boolean, id:Int)

}