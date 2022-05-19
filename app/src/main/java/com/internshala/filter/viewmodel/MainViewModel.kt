package com.internshala.filter.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.internshala.filter.model.ImageEntity
import com.internshala.filter.repo.RemoteRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val apiRepo: RemoteRepo ) : ViewModel() {

    fun postImage(image:ImageEntity , context: Context){
        apiRepo.postImage(image).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (!response.body().isNullOrEmpty())
                    Toast.makeText(context,"${response.body()}",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context,"Something Went Wrong " , Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Something Went Wrong " , Toast.LENGTH_LONG).show()
            }

        })
    }
}

