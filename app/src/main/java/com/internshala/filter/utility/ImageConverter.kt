package com.internshala.filter.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.ByteArrayOutputStream

class ImageConverter(private val context: Context) {

     suspend fun getBitmap(uri: Uri?): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

     fun toByteArray(bitmap: Bitmap):ByteArray{
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outStream)
        return outStream.toByteArray()
    }

    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}