package com.fernando.prueba.models

import androidx.room.Entity


@Entity
data class Post(
    var userId:Int?=null,
    var id:Int?=null,
    var title:String?=null,
    var body:String?=null
)
