package com.fernando.prueba

import android.app.Application
import com.fernando.prueba.data.AppDatabase
import com.fernando.prueba.interceptors.NetworkInterceptorConnection
import com.fernando.prueba.models.Post
import com.fernando.prueba.network.ApiService
import com.fernando.prueba.ui.post.PostRepository
import com.fernando.prueba.ui.post.PostViewModelFactory
import com.fernando.prueba.utils.BuildVersion
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*


class MVVMApplication:Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { Post() }

        bind() from singleton { BuildVersion() }

        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { NetworkInterceptorConnection(instance(), instance()) }

        bind() from singleton { ApiService(instance()) }

        //Todo dependency injection Post

        bind() from singleton { PostRepository(instance(), instance()) }
        bind() from provider { PostViewModelFactory(instance()) }

    }
}