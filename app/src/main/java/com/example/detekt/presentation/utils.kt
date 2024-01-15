package com.example.detekt.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.detekt.MainActivity
import java.io.File

fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken:(Bitmap)->Unit,
    context: Context
){
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback(){
            // if we successfully capture an img
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,0,
                    image.width,image.height,
                    matrix,true
                )

                onPhotoTaken(rotatedBitmap)
            }
            // if we fail to capture an image
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera","Could not take a picture",exception)
            }
        }
    )
}

@SuppressLint("MissingPermission")
fun recordVideo(
    controller: LifecycleCameraController,
    recording: Recording?,
    parent: File?,
    context: Context
){
    var rec = recording
    if (rec!=null){
        rec.stop()
        rec = null
        return
    }


    val outputOptions = File(parent,"my-recordings.mp4")
    controller.startRecording(
        FileOutputOptions.Builder(outputOptions).build(),
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(context)
    ){event->
        when(event){
            is VideoRecordEvent.Finalize->{
                if (event.hasError()){
                    rec?.close()
                    rec = null

                    Toast.makeText(
                        context,
                        "Could not take the video",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    Toast.makeText(
                        context,
                        "Video captured successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun PhotoBottomSheetContent(
    bitmaps:List<Bitmap>,
    modifier: Modifier=Modifier
){
    if (bitmaps.isEmpty()){
        Box(modifier = modifier.padding(16.dp),
            contentAlignment = Alignment.Center ){
            Text(text = "There are no photos taken yet")
        }
    }
    else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(120.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ) {
            items(bitmaps) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

















