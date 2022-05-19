package com.internshala.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.internshala.filter.repo.RemoteRepo

class GalleryViewModelFactory constructor(private val repository : RemoteRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            GalleryViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}