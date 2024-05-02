package com.example.amusegrind.recorder.presentation

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.data.AudiosRepo
import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.recorder.domain.LoadProgress
import com.example.amusegrind.recorder.domain.LocalAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecorderViewmodel @Inject constructor(
    private val navigator: Navigator,
    private val audiosRepo: AudiosRepo,
    private val userRepo: UserRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(RecorderState())
    val state: StateFlow<RecorderState> = _state.asStateFlow()

    private var mRecorder: MediaRecorder? = null
    private var mFileName: String = ""

    init {
        val externalCacheDir = context.applicationContext.externalCacheDir?.absolutePath
        mFileName = "$externalCacheDir/${UUID.randomUUID()}"
    }

    fun postAudio(newLocalAudioPath: String) {

        _state.update { it.copy(recordingStatus = LoadProgress.ACTIVE) }
        viewModelScope.launch {

            val localaudio = LocalAudio(filePath = newLocalAudioPath, 9, LocalDate.MIN.toString())

            localaudio.filePath?.let { localAudioUri ->
                audiosRepo.uploadAudio(localAudioUri).first().onSuccess { url ->
                    audiosRepo.saveVideoToFireDB(
                        isPrivate = false,
                        videoUrl = url.toString(),
                        descriptionText = "DESCRIPTION YANDEX SPEECH KIT, DESCRIPTION YANDEX SPEECH KIT, DESCRIPTION YANDEX SPEECH KIT",
                        duration = localaudio.duration,
                        onComplete = { succeeded ->
                            _state.update { it.copy(recordingStatus = if (succeeded) LoadProgress.DONE else LoadProgress.FAILED) }
                        }
                    )
                }.onFailure {
                    _state.update { it.copy(recordingStatus = LoadProgress.FAILED) }
                }
            }

        }
    }

    fun startRecording() {
        viewModelScope.launch {
            mRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(mFileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e("AudioViewModel", "prepare() failed")
                }
                start()
            }
        }
    }

    fun stopRecording() {
        mRecorder?.apply {
            stop()
            release()
        }
        mRecorder = null
        convertToMp3(mFileName)?.let { postAudio(it) }
    }

    override fun onCleared() {
        mRecorder?.release()
        super.onCleared()
    }
}

fun convertToMp3(localAudioPath: String): String? {
    val outputFilePath = "$localAudioPath.mp3"
    val command = "-i $localAudioPath -codec:a libmp3lame $outputFilePath"
    val resultCode = FFmpeg.execute(command)
    return if (resultCode == 0) outputFilePath else null
}

