package com.mskstudios.dodger

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebSettings

class MainActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        myWebView = findViewById(R.id.webview)
        val webSettings: WebSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false

        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")
        myWebView.loadUrl("file:///android_asset/index.html")
    }

    /**
     * This new onDestroy function is crucial. It ensures that when the user
     * closes the app, the WebView is properly destroyed, stopping any
     * background processes like music.
     */
    override fun onDestroy() {
        // Destroy the WebView to prevent music from playing in the background
        myWebView.loadUrl("about:blank") // Clear the content
        myWebView.stopLoading()
        myWebView.destroy()
        super.onDestroy()
    }

    class WebAppInterface(private val mContext: Context) {
        @JavascriptInterface
        fun quitApp() {
            if (mContext is Activity) {
                mContext.finish()
            }
        }
    }
}