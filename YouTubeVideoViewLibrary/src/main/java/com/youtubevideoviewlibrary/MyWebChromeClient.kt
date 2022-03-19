package com.youtubevideoviewlibrary

import android.app.Activity
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.VideoView

class MyWebChromeClient(private val callbacks: Callbacks) : WebChromeClient(), OnPreparedListener, OnCompletionListener,
    MediaPlayer.OnErrorListener {

    data class Callbacks(
        val script: ()->String,
        val notifyTime: (String,Float)->Unit,
        val onFullScreenChanged: (Boolean)->Unit,
        val loadingView: ()->View?,
        val addVideoContainer: (Float,String,FrameLayout?)->Unit,
        val removeVideoContainer: (Float,FrameLayout?)->Unit
    )

    var isVideoFullscreen = false
    get(){
        return field
    }
    private set(value){
        field = value
    }
    var webView: YoutubeView? = null
    private var videoViewContainer: FrameLayout? = null
    private var videoViewCallback: CustomViewCallback? = null

    fun getScript(): String{
        return callbacks.script()
    }

    fun videoContainer(): FrameLayout?{
        return videoViewContainer
    }

    fun callAddVideoContainer(time: Float = 0f, lastUrl: String){
        callbacks.addVideoContainer(time,lastUrl,videoViewContainer)
        (webView?.context as? Activity)?.runOnUiThread {
            webView?.play()
        }
    }

    fun callRemoveVideoContainer(time: Float = 0f){
        callbacks.removeVideoContainer(time,videoViewContainer)
        videoViewContainer = null
        videoViewCallback = null
    }

    fun notifyTime(tag: String,currentTime: Float){
        callbacks.notifyTime(tag,currentTime)
    }

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        super.onShowCustomView(view, callback)
        if (view is FrameLayout) {
            val frameLayout = view
            val focusedChild = frameLayout.focusedChild
            isVideoFullscreen = true
            this.videoViewContainer = frameLayout
            this.videoViewCallback = callback
            webView?.setHook()
            webView?.requestProgress(YoutubeView.TAG_ENTER_FULLSCREEN)
            if (focusedChild is VideoView) {
                val videoView = focusedChild
                val p = videoView.currentPosition
                Log.d("youtube_video","progress=$p")
                videoView.setOnPreparedListener(this)
                videoView.setOnCompletionListener(this)
                videoView.setOnErrorListener(this)
            }
            callbacks.onFullScreenChanged(true)
        }
    }

    fun exitFullScreen(){
        videoViewCallback?.onCustomViewHidden()
    }

    override fun onShowCustomView(
        view: View,
        requestedOrientation: Int,
        callback: CustomViewCallback
    )
    {
        onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        webView?.requestProgress(YoutubeView.TAG_EXIT_FULLSCREEN)
        if (isVideoFullscreen) {
            if (videoViewCallback != null && !videoViewCallback!!.javaClass.name.contains(".chromium.")) {
                videoViewCallback!!.onCustomViewHidden()
            }
            isVideoFullscreen = false
            callbacks.onFullScreenChanged(false)
        }
    }

    override fun getVideoLoadingProgressView(): View?
    {
        return callbacks.loadingView()?.apply {
            visibility = View.VISIBLE
        }?: super.getVideoLoadingProgressView()
    }

    override fun onPrepared(mp: MediaPlayer)
    {
        callbacks.loadingView()?.visibility = View.GONE
    }

    override fun onCompletion(mp: MediaPlayer)
    {
        onHideCustomView()
    }

    override fun onError(
        mp: MediaPlayer,
        what: Int,
        extra: Int
    ): Boolean
    {
        return false
    }

    fun onBackPressed(): Boolean {
        return if (isVideoFullscreen) {
            onHideCustomView()
            true
        } else {
            false
        }
    }
}