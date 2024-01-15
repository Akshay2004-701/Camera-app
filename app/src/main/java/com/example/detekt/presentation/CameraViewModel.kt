package com.example.detekt.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel:ViewModel() {

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    // add the taken photo to the bitmap list
    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value += bitmap
    }
}