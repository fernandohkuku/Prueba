package com.fernando.prueba.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fernando.prueba.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {


    private val binding: ActivityPostBinding by viewBinding();



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
}