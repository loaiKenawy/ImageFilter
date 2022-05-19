package com.internshala.filter.remote

import com.internshala.filter.model.ImageEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {


    @GET("/image")
    fun getAll() : Call<List<ImageEntity>>

    @POST("/post")
    fun postImage(@Body string:ImageEntity):Call<String>

}