package com.example.amusegrind.core.utils

import android.media.MediaMetadataRetriever
import com.arthenica.mobileffmpeg.FFmpeg

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
}
