package com.algogence.articleview

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioPlayer{
    private var job: Job? = null
    private var pauseRequested: Boolean = false
        private var visualizer: Visualizer? = null
        private var url: String? = null
        private var mediaPlayer: MediaPlayer? = null
        private var preparing = false
        private var prepared = false
        fun play(url: String?){
            if(url==null){
                return
            }
            if(prepared||preparing){
                stop()
            }
            this.url = url
            job = CoroutineScope(Dispatchers.IO).launch{
                assureMediaPlayer()
                setupStreamMode()
                startPlaying()
            }
        }

        fun resume(){
            pauseRequested = false
            if(prepared&&mediaPlayer?.isPlaying==false){
                mediaPlayer?.start()
            }
        }

        fun pause(){
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
            else{
                pauseRequested = true
            }
        }

        fun stop() {
            releaseVisualizer()
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            job?.cancel()
            mediaPlayer = null
            preparing = false
            prepared = false
            completed = true
            bufferingProgress = 0
            progress = Progress()
            callListeners()
        }

        private fun releaseVisualizer() {
            if (visualizer == null) return
            visualizer!!.release()
        }

        private fun startPlaying() {
            try {
                mediaPlayer?.setDataSource(url)
                mediaPlayer?.prepare()
                if(pauseRequested){
                    pauseRequested = false
                }
                else{
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
            }
        }

        private fun setupStreamMode() {
            if(preparing){
                return
            }
            preparing = true
            prepared = false
            callListeners()
            mediaPlayer?.setOnPreparedListener {
                prepared = true
                preparing = false
                callListeners()
                setupVisualizer()
            }
            mediaPlayer?.setOnBufferingUpdateListener { mediaPlayer, i ->
                onBufferingUpdate(i)
            }
            mediaPlayer?.setOnCompletionListener {
                onCompleted()
                stop()
            }
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }

        data class Status(
            val preparing: Boolean = false,
            val prepared: Boolean = false,
            val completed: Boolean = false,
            val bufferingProgress: Int = 0,
            val isPlaying: Boolean = false
        )
        var completed = false
        var bufferingProgress = 0
        private fun onCompleted() {
            completed = true
            completed = false
            bufferingProgress = 0
            progress = Progress()
            callListeners()
        }

        private fun callListeners() {
            val status = Status(
                preparing,
                prepared,
                completed,
                bufferingProgress,
                mediaPlayer?.isPlaying?:false
            )
            listeners.forEach {
                it(progress,status)
            }
        }

        private fun onBufferingUpdate(i: Int) {
            bufferingProgress = i
            callListeners()
        }

        private fun setupVisualizer() {
            visualizer = mediaPlayer?.audioSessionId?.let { Visualizer(it) }
            visualizer?.enabled = false
            visualizer?.captureSize = Visualizer.getCaptureSizeRange()[1]

            visualizer?.setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer, bytes: ByteArray,
                    samplingRate: Int
                ) {
                    onWaveForm(bytes,samplingRate)
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer, bytes: ByteArray,
                    samplingRate: Int
                ) {
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false)

            visualizer?.enabled = true
        }

        var progress = Progress()
        private fun onWaveForm(bytes: ByteArray, samplingRate: Int) {
            val progress = mediaPlayer?.currentPosition
            val total = mediaPlayer?.duration
            val percentage: Float = when{
                progress==null->0f
                total==null->0f
                total==0->0f
                else->progress.toFloat()/total.toFloat()
            }
            onProgress(bytes,samplingRate,progress,total,percentage)
        }
        fun setListener(listener: ((Progress,Status)->Unit)?){
            listeners.clear()
            if (listener != null) {
                listeners.add(listener)
            }
        }
        data class Progress(
            val waveForm: ByteArray = ByteArray(0),
            val samplingRate: Int = 0,
            val progress: Int? = 0,
            val total: Int? = 0,
            val progressFactor: Float = 0f
        )

        private val listeners = mutableListOf<(Progress,Status)->Unit>()

        fun addListener(listener: (Progress,Status)->Unit){
            listeners.add(listener)
        }
        fun removeListener(listener: (Progress,Status)->Unit){
            listeners.remove(listener)
        }

        private fun onProgress(
            bytes: ByteArray,
            samplingRate: Int,
            progress: Int?,
            total: Int?,
            percentage: Float
        ) {
            this.progress = Progress(
                bytes,
                samplingRate,
                progress,
                total,
                percentage
            )

            callListeners()
        }

        private fun assureMediaPlayer() {
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer()
            }
        }

        fun removeListeners() {
            listeners.clear()
        }

        fun seekToFactor(it: Float) {
            mediaPlayer?.seekTo((it*(mediaPlayer?.duration?:0)).toInt())
        }

        fun playPause(url: String?) {
            if(mediaPlayer?.isPlaying==true){
                pause()
            }
            else{
                if(!prepared&&!preparing){
                    play(url)
                }
                resume()
            }
        }
    }