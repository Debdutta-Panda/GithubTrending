package com.algogence.articleview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity


class WebPageActivity : AppCompatActivity() {
    private var isError = false
    private var url = ""
    inner class MyWebViewClient: WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            if(!isError){
                cl_overlay?.visibility = View.GONE
            }
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if(failingUrl==url||failingUrl=="$url/"){
                isError = true
            }
            cl_overlay?.visibility = View.VISIBLE
        }
    }
    inner class MyJavaScriptInterface internal constructor(private val c: Context) {
        @Keep
        @JavascriptInterface
        fun pageLoaded() {
            cl_overlay?.visibility = View.GONE
        }
    }
    private var game_view: WebView? = null
    private var cl_overlay: View? = null
    private var tv_try_again: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent?.getStringExtra("url")?:""
        setContentView(R.layout.activity_game)
        game_view = findViewById(R.id.game_view)
        cl_overlay = findViewById(R.id.cl_overlay)
        tv_try_again = findViewById(R.id.tv_try_again)
        tv_try_again?.setOnClickListener {
            cl_overlay?.visibility = View.VISIBLE
            loadPage()
        }
        if(savedInstanceState==null){
            game_view?.settings?.javaScriptEnabled = true
            game_view?.webViewClient = MyWebViewClient()
            game_view?.webChromeClient = WebChromeClient()
            //game_view?.addJavascriptInterface(MyJavaScriptInterface(this),"android")
            loadPage()
        }
    }

    private fun loadPage() {
        isError = false
        game_view?.loadUrl(url)
    }

    override fun onBackPressed() {
        if(game_view?.canGoBack()==true){
            game_view?.goBack()
        }
        else{
            super.onBackPressed()
        }
    }
}