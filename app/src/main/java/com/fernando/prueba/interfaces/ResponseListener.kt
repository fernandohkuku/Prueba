package com.fernando.prueba.interfaces

interface ResponseListener {
    fun onStarted()
    fun onFailure(message:String):Unit
    fun onSuccess()
}