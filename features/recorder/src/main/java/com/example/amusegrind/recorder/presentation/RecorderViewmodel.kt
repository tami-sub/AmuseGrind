package com.example.amusegrind.recorder.presentation

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.amusegrind.core.utils.FileHelper.convertToOGG
import com.example.amusegrind.core.utils.FileHelper.getAudioFileDuration
import com.example.amusegrind.navigator.Navigator
import com.example.amusegrind.network.data.AudiosRepo
import com.example.amusegrind.network.data.UserRepo
import com.example.amusegrind.network.data.YandexSpeechKitService
import com.example.amusegrind.recorder.domain.entities.LoadProgress
import com.example.amusegrind.recorder.domain.entities.LocalAudio
import com.example.amusegrind.recorder.domain.usecase.SpeechRecognizerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RecorderViewmodel @Inject constructor(
    private val navigator: Navigator,
    private val audiosRepo: AudiosRepo,
    private val userRepo: UserRepo,
    private val speechRecognizerUseCase: SpeechRecognizerUseCase,
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
            val localAudio = LocalAudio(
                filePath = newLocalAudioPath,
                duration = getAudioFileDuration(newLocalAudioPath)/100,
                dateCreated = LocalDate.MIN.toString()
            )
            val audioType = "audio/x-pcm;bit=16;rate=16000".toMediaType()
            val requestBody = File(newLocalAudioPath).readBytes().toRequestBody(audioType)
            try {
                speechRecognizerUseCase(requestBody = requestBody).onSuccess {
                    uploadAndSaveFileToDb(localaudio = localAudio, it.result)

                }.onFailure {
                    Log.d("joka", "Failed to recognize speech: ${it.message}")
                }
            } catch (t: Throwable) {
                Log.d("joka", "Error: ${t.localizedMessage}")
            }
        }
    }

    private suspend fun uploadAndSaveFileToDb(localaudio: LocalAudio, descriptionText: String) {
        localaudio.filePath?.let { localAudioUri ->
            audiosRepo.uploadAudio(localAudioUri).first().onSuccess { url ->
                audiosRepo.saveVideoToFireDB(
                    isPrivate = false,
                    videoUrl = url.toString(),
                    descriptionText = descriptionText,
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

    @RequiresApi(Build.VERSION_CODES.Q)
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
//        postAudio(mFileName)
        convertToOGG(mFileName)?.let { postAudio(it) }
    }

    override fun onCleared() {
        mRecorder?.release()
        super.onCleared()
    }
}
