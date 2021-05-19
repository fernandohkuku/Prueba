package com.fernando.prueba.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Post(
    var userId:Int?=null,
    @PrimaryKey
    var id:Int?=null,
    var title:String?=null,
    var body:String?=null,
    var isFavorite:Boolean?=null,
    @ColumnInfo
    var isRead:Boolean?=false
):Serializable{
}
