package com.example.amusegrind.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Base64
import android.util.Log
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import java.io.File

object FileHelper {
    fun convertToMp3(localAudioPath: String): String? {
        val outputFilePath = "$localAudioPath.mp3"
        val command = "-i $localAudioPath -codec:a libmp3lame $outputFilePath"
        val resultCode = FFmpeg.execute(command)
        return if (resultCode == 0) outputFilePath else null
    }

    fun convertToOGG(localAudioPath: String): String? {
        val outputFilePath = "$localAudioPath.ogg"
        val command = "-i $localAudioPath -codec:a libvorbis $outputFilePath"
        val resultCode = FFmpeg.execute(command)
        return if (resultCode == 0) outputFilePath else null
    }

    fun getAudioFileDuration(filePath: String): Long {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(filePath)
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            return durationStr?.toLong() ?: 0L
        } finally {
            retriever.release()
        }
    }

    fun decodeBase64ToImageFile(base64Image: String, context: Context): File {
        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        val imageFile = File(context.cacheDir, "decoded_image.png")
        imageFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return imageFile
    }

    fun combineImageAndAudio(imageBase64: String, audioPath: String, context: Context): String {
        try {
            // Extracting video name without extension from the audio path
            val newVideoPath = audioPath.substringBefore(".ogg")
            // Decode base64 to an image file and get its path
            val imagePath = decodeBase64ToImageFile(imageBase64, context).absolutePath
            // Define the path for the output video file
            val videoOutputPath = File(context.cacheDir, "$newVideoPath.mp4").absolutePath

            // FFmpeg command to merge image and OGG audio into an MP4 video
//            val cmd = "-loop 1 -i $imagePath -i $audioPath -c:v libx264 -tune stillimage -c:a aac -b:a 192k -pix_fmt yuv420p -shortest -y $videoOutputPath"
            val simpleCmd = "-loop 1 -i $imagePath -c:v libx264 -tune stillimage -pix_fmt yuv420p -t 10 -y $videoOutputPath"

            // Execute FFmpeg command
            val rc = FFmpeg.execute(simpleCmd)

            if (rc == RETURN_CODE_SUCCESS) {
                Log.d("joka", "Video successfully created: $videoOutputPath")
                return videoOutputPath
            } else {
                Log.e("joka", "FFmpeg failed with return code $rc")
                return audioPath
            }
        } catch (e: Exception) {
            Log.e("joka", "Failed to convert into video", e)
            return audioPath
        }
    }
}
