package com.internshala.filter.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.internshala.filter.model.ImageEntity
import com.internshala.filter.repo.RemoteRepo
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryViewModel(private val apiRepo: RemoteRepo) : ViewModel() {

    val imageList = MutableLiveData<List<ImageEntity>>()
    var failure : Boolean = true
     suspend fun getAllImages(context: Context) {
        val response = apiRepo.getAllPhotos()
         delay(500)
        response.enqueue(object : Callback<List<ImageEntity>> {
            override fun onResponse(
                call: Call<List<ImageEntity>>,
                response: Response<List<ImageEntity>>
            ) {
                failure = false
                imageList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<ImageEntity>>, t: Throwable) {
                failure = true
                Toast.makeText(context,"Something Went Wrong " , Toast.LENGTH_LONG).show()
            }

        })
    }
}