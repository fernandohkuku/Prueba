package com.fernando.prueba.ui.user

import com.fernando.prueba.data.AppDatabase
import com.fernando.prueba.network.ApiService
import com.fernando.prueba.network.SafeApiRequest

class UserRepository(
    private val apiService: ApiService,
    private val db:AppDatabase
):SafeApiRequest(){

    internal suspend fun getUser(id:String) = apiRequest { apiService.getUser(id) }

    internal suspend fun setFavorite(isFavorite:Boolean, id:Int) = db.dao().setFavorite(isFavorite, id)

}