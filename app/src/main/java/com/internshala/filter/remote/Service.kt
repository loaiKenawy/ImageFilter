package com.internshala.filter.remote

import android.content.Context
import android.provider.Settings.Global.getString
import com.internshala.filter.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service {


    companion object {
        var retrofitService: API? = null

        fun getInstance() : API? {
            if (retrofitService == null) {
                //change baseUrl to your IP address-->
                //IP ex: xxx.xxx.xxx.xxx:
                val baseURL = ""
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(API::class.java)
            }
            return retrofitService
        }
    }

}
