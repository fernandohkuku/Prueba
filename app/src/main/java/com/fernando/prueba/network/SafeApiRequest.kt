package com.fernando.prueba.network

import com.fernando.prueba.utils.ApiServiceException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call:suspend ()-> Response<T>):T?{
        call.invoke().also {response->
            if (response.isSuccessful){
                return  response.body();
            }else{
                val error = response.errorBody()?.string();
                val message = StringBuilder();
                error?.let{response->
                    try {
                        val iterator:Iterator<String?> = JSONObject(response).keys()
                        while (iterator.hasNext()){
                            val key = iterator.next();
                            message.append(JSONObject(response).getString(key))
                        }
                    }catch (e:JSONException){

                    }
                }
                throw ApiServiceException(message.toString());
            }
        }
    }

}