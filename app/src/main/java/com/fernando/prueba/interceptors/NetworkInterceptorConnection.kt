package com.fernando.prueba.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.fernando.prueba.utils.BuildVersion
import com.fernando.prueba.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response


class NetworkInterceptorConnection(
    context: Context,
    private val buildVersion: BuildVersion
) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoInternetException("No tienes conexión a internet")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            if (buildVersion.isMarshmallowAndAbove()) {
                it.getNetworkCapabilities(it.activeNetwork)?.apply {
                    if (hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED
                        )
                    ) {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            else -> false
                        }
                    }
                }
            } else {
                it.activeNetworkInfo.also { networkInfo ->
                    result = networkInfo != null && networkInfo.isConnected
                }
            }
        }
        return result
    }
}