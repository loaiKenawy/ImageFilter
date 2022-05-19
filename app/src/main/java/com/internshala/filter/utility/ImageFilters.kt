package com.internshala.filter.utility

import android.graphics.Bitmap
import android.graphics.Color

class ImageFilters {

    fun filterBlackAndWhite(originalImage: Bitmap): Bitmap {
        val width = originalImage.width
        val height = originalImage.height
        val blackWhiteImage = Bitmap.createBitmap(width, height, originalImage.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var colorPixel: Int

        for (x in 0 until width) {
            for (y in 0 until height) {
                colorPixel = originalImage.getPixel(x, y)
                A = Color.alpha(colorPixel)
                R = Color.red(colorPixel)
                G = Color.green(colorPixel)
                B = Color.blue(colorPixel)

                R = (R + G + B) / 3
                G = R
                B = R
                blackWhiteImage.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        return blackWhiteImage
    }


    fun filterBlur(originalImage: Bitmap, threshHold : Int): Bitmap {
        val width = originalImage.width
        val height = originalImage.height
        val blackWhiteImage = Bitmap.createBitmap(width, height, originalImage.config)
        var A: Int = 0
        var R: Int
        var G: Int
        var B: Int
        var tempR: Int = 0
        var tempG: Int = 0
        var tempB: Int = 0
        var colorPixel: Int
        var shift: Int = 1
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (shift == threshHold) {
                    colorPixel = originalImage.getPixel(x, y)
                    A = Color.alpha(colorPixel)
                    R = Color.red(colorPixel)
                    G = Color.green(colorPixel)
                    B = Color.blue(colorPixel)
                    tempR = R
                    tempG = G
                    tempB = B
                    blackWhiteImage.setPixel(x, y, Color.argb(A, R, G, B))
                    shift = 0
                } else {
                    blackWhiteImage.setPixel(x, y, Color.argb(A, tempR, tempG, tempB))
                    shift++
                }
            }
        }
        return blackWhiteImage
    }

}