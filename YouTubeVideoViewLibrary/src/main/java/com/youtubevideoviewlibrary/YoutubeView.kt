package com.youtubevideoviewlibrary;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer


class YoutubeView : WebView {
    private var activityResolver: (()->Activity?)? = null
    var networkAware = true
    var con: ConnectivityListener? = null
    val obs = Observer<ConnectivityListener.Net> {
        setUrl(lastUrl)
    }
    private var lastTag = ""
    companion object{
        val map = mutableMapOf<String,Int>()
        val TAG_ENTER_FULLSCREEN = "enter_fullscreen"
        val TAG_EXIT_FULLSCREEN = "exit_fullscreen"
        fun timeEntry(tag: String, time: Int){
            map[tag] = time
        }
        fun getEntry(tag: String): Int{
            return map.remove(tag)?:-1
        }
    }

    private var fullscreenEnabled = false
    private var videoEnabledWebChromeClient: MyWebChromeClient? = null
    private var addedJavascriptInterface = false
    inner class JavascriptInterface {
        @android.webkit.JavascriptInterface
        fun notifyVideoEnd(currentTime: Float) // Must match Javascript interface method of VideoEnabledWebChromeClient
        {
            Log.d("youtube_video", "ended=$currentTime")
            // This code is not executed in the UI thread, so we must force that to happen
            Handler(Looper.getMainLooper()).post {
                videoEnabledWebChromeClient?.onHideCustomView()
            }
        }
        @android.webkit.JavascriptInterface
        fun notifyProgress(tag: String,currentTime: Float) // Must match Javascript interface method of VideoEnabledWebChromeClient
        {
            Log.d("youtube_video", "progress=$currentTime")
            if(tag== TAG_ENTER_FULLSCREEN){
                if(videoEnabledWebChromeClient?.videoContainer()!=null){
                    videoEnabledWebChromeClient?.callAddVideoContainer(currentTime,lastUrl)
                }
            }
            else if(tag== TAG_ENTER_FULLSCREEN){
                if(videoEnabledWebChromeClient?.videoContainer()!=null){
                    videoEnabledWebChromeClient?.callRemoveVideoContainer(currentTime)
                }
            }
            else{
                videoEnabledWebChromeClient?.notifyTime(tag,currentTime)
            }
        }
    }

    var isVideoFullscreen = false
        get(){
            return videoEnabledWebChromeClient?.isVideoFullscreen?:false
        }

    /*var net = App.net*/
    /*lateinit var observer: Observer<ConnectivityListener.Net>*/
    private var lastUrl = ""
    private var lastLoaded = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun setup(fullscreenEnabled: Boolean, playInstantly: Boolean=false,callbacks: MyWebChromeClient.Callbacks?=null){
        this.playInstantly = playInstantly
        this.fullscreenEnabled = fullscreenEnabled
        settings.defaultTextEncodingName = "utf-8"
        settings.javaScriptEnabled = true
        settings.pluginState = WebSettings.PluginState.ON
        if(fullscreenEnabled&&callbacks!=null){
            videoEnabledWebChromeClient = MyWebChromeClient(callbacks)
            videoEnabledWebChromeClient?.webView = this
            webChromeClient = videoEnabledWebChromeClient
        }
        else{
            webChromeClient = WebChromeClient()
        }
        webViewClient = WebViewClient()
    }

    private fun init() {
        settings.defaultTextEncodingName = "utf-8"
        settings.javaScriptEnabled = true
        settings.pluginState = WebSettings.PluginState.ON


        if(networkAware){
            con = ConnectivityListener(context)
            con?.net?.observeForever(obs)
        }

        /*webChromeClient = MyWebChromeClient(MyWebChromeClient.Callbacks(

        ))
        webViewClient = WebViewClient()*/

        /*observer = Observer<ConnectivityListener.Net> { t ->
            setUrl(lastUrl)
        }
        net.observeForever(observer)*/
    }

    private var playInstantly = false
    fun setVideo(fullscreenEnabled: Boolean,url: String = "", id: String = "", startTime: String = "",activityResolver: (()->Activity?)?){
        this.activityResolver = activityResolver
        /*****************************/
        setup(fullscreenEnabled,false,
            MyWebChromeClient.Callbacks(
                script = {
                    ""
                },
                notifyTime = { tag, it ->

                },
                onFullScreenChanged = {

                },
                addVideoContainer = { time, url, it ->
                    /*runOnUiThread {
                        findViewById<ViewGroup>(R.id.videoLayout)
                            .addView(
                                it,
                                ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                ))
                    }*/
                    getMyActivity()?.apply {
                        runOnUiThread {
                            val intent = Intent()
                            intent.setClassName(
                                this,
                                "com.youtubevideoviewlibrary.VideoActivity"
                            ).apply {
                                putExtra("url", url)
                                putExtra("time", time.toInt())
                                lastTag = System.currentTimeMillis().toString() + "_yt"
                                putExtra("tag", lastTag)
                            }
                            startActivity(intent)
                            /*startActivity(

                                Intent(
                                    this,
                                    VideoActivity::class.java
                                ).apply {
                                    putExtra("url", url)
                                    putExtra("time", time.toInt())
                                    lastTag = System.currentTimeMillis().toString() + "_yt"
                                    putExtra("tag", lastTag)
                                })*/
                        }
                    }
                },
                removeVideoContainer = { time, it ->
                    /*runOnUiThread {
                        findViewById<ViewGroup>(R.id.videoLayout)
                            .removeView(it)
                    }*/
                },
                loadingView = {
                    null
                }
            ))
        /*****************************/
        var resolvedUrl = ""
        if(url.isNotEmpty()){
            resolvedUrl = url
        }
        else if(id.isNotEmpty()){
            resolvedUrl = "https://www.youtube.com/embed/$id"
        }
        if(resolvedUrl.isNotEmpty()){
            if(startTime.isNotEmpty()){
                resolvedUrl = resolvedUrl.replace("\\?start=.*".toRegex(),"")
                resolvedUrl += "?start=$startTime"
            }
            setUrl(resolvedUrl)
        }
    }

    private fun getMyActivity(): Activity? {
        return activityResolver?.invoke()
        /*var c = context
        while (c is ContextWrapper) {
            if (c is Activity) {
                return c
            }
            c = c.baseContext
        }
        return null*/
    }

    private fun setUrl(url: String) {
        if (url.isEmpty()) {
            return
        }
        lastUrl = url
        if(networkAware){
            val connection = con?.isConnected()
            if (connection?.on==true) {
                if (connection.metered) {
                    //context.toast(R.string.video_on_metered_warning.string())
                }
                loadUrl(lastUrl)
                lastLoaded = true
            } else {
                loadNoInternet()
            }
        }
        else{
            loadUrl(lastUrl)
        }
    }



    private fun loadNoInternet() {
        //loadUrl("file:///android_asset/video_internet_error.html")
        loadDataWithBaseURL(null, getNoInternetContent(), "text/html", "utf-8", null);
    }

    private fun getNoInternetContent(): String {
        return VideoNoInternetContent.compose()
        return ""
    }

    override fun onDetachedFromWindow() {
        con?.net?.removeObserver(obs)
        con?.destroy()
        con = null
        super.onDetachedFromWindow()
    }

    /*override fun loadUrl(url: String?) {
        addJavascriptInterface()
        super.loadUrl(url!!)
    }

    fun loadUrl(url: String?, additionalHttpHeaders: Map<String?, String?>?) {
        addJavascriptInterface()
        super.loadUrl(url!!, additionalHttpHeaders!!)
    }*/

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        addJavascriptInterface()
        super.loadUrl(url, additionalHttpHeaders)
        setHook()
    }

    fun requestProgress(tag: String = ""){
        super.loadUrl("javascript:notifyProgress('$tag');")
    }

    fun play(){
        super.loadUrl("javascript:play()")
    }

    private val defaultScript = """
                        javascript:
                        var _ytrp_html5_video_last;
                        var _ytrp_html5_video = document.getElementsByTagName('video')[0];
                        if (_ytrp_html5_video != undefined && _ytrp_html5_video != _ytrp_html5_video_last) {
                            _ytrp_html5_video_last = _ytrp_html5_video;
                            function _ytrp_html5_video_ended() {
                                _VideoEnabledWebView.notifyVideoEnd(_ytrp_html5_video.currentTime);
                            }
                            _ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);
                        }
                        function notifyProgress(tag){
                            _VideoEnabledWebView.notifyProgress(tag,_ytrp_html5_video.currentTime)
                        }
                        function play(){
                            _ytrp_html5_video_last.oncanplay = function(){
                                _ytrp_html5_video_last.play();
                                _ytrp_html5_video_last.oncanplay = null
                            }
                        }
                        function seekTo(time){
                            _ytrp_html5_video_last.currentTime = time;
                        }
                    """.trimIndent().replace("\n","")

    fun setHook(){
        var script = videoEnabledWebChromeClient?.getScript()?:""
        if(script.isEmpty()){
            script = defaultScript
        }
        super.loadUrl(script)
    }

    fun seekTo(time: Int){
        super.loadUrl("javascript:seekTo($time)")
    }

    override fun loadUrl(url: String) {
        addJavascriptInterface()
        super.loadUrl(url)
        setHook()
    }

    private fun addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            // Add javascript interface to be called when the video ends (must be done before page load)
            addJavascriptInterface(
                JavascriptInterface(),
                "_VideoEnabledWebView"
            ) // Must match Javascript interface name of VideoEnabledWebChromeClient
            addedJavascriptInterface = true
        }
    }

    fun onBackPressed(): Boolean{
        return if(videoEnabledWebChromeClient?.onBackPressed()==true){
            if (canGoBack()) {
                goBack()
                true
            } else {
                false
            }
        } else{
            false
        }
    }

    fun exitFullScreen() {
        val time = getEntry(lastTag)
        videoEnabledWebChromeClient?.exitFullScreen()
        if(time > -1){
            seekTo(time)
        }
    }




}

/*
@BindingAdapter("app:videoId")
fun YoutubeView.setVideoId(id: String?) {
    if (id != null) {
        setUrl("$id?autoplay=0&vq=small")
    }
}*/
