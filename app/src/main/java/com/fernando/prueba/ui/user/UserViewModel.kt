package com.fernando.prueba.ui.user

import androidx.lifecycle.ViewModel

class UserViewModel(
    private val repository: UserRepository
):ViewModel(){

    suspend fun getUser(id:String) = repository.getUser(id);
    suspend fun setFavorite(isFavorite:Boolean,id:Int) = repository.setFavorite(isFavorite, id)
}