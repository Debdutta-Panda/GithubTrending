package com.youtubevideoviewlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView

class VideoActivity : AppCompatActivity() {
    private var yv_video: YoutubeView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video)
        WebView.setWebContentsDebuggingEnabled(true)

        yv_video = findViewById<View>(R.id.yv_video) as YoutubeView

        yv_video?.setup(true,true,
            MyWebChromeClient.Callbacks(
                script = {
                    ""
                },
                notifyTime = { tag, it ->
                    if (tag == "on_back") {
                        YoutubeView.timeEntry(intent?.getStringExtra("tag") ?: "", it.toInt())
                        runOnUiThread {
                            finish()
                        }
                    }
                },
                onFullScreenChanged = {

                },
                addVideoContainer = { time, url, it ->
                },
                removeVideoContainer = { time, it ->
                },
                loadingView = {
                    null
                })
        )
        yv_video?.setVideo(false,intent?.getStringExtra("url")?:"", startTime = (intent?.getIntExtra("time",0)?:0).toString()){
            this
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        yv_video?.setHook()
        yv_video?.requestProgress("on_back")
    }
}