package com.internshala.filter.repo

import com.internshala.filter.model.ImageEntity
import com.internshala.filter.remote.API

class RemoteRepo(private val service: API) {
    fun getAllPhotos() = service.getAll()
    fun postImage(image:ImageEntity) = service.postImage(image)

}