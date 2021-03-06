package com.rk.mykotlingameengine.graphics

class Buffer2D(val width: Int, val height: Int) {

    val pixels: FloatArray = FloatArray(width * height)

    fun clear() {
        for (i in 0 until width * height) {
            pixels[i] = 0.0f
        }
    }
}