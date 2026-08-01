package com.rassvet.app

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private var isLocked = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(LockBridge(), "AndroidLock")
        webView.loadUrl("file:///android_asset/rassvet.html")
    }

    inner class LockBridge {
        @JavascriptInterface
        fun lock() {
            runOnUiThread {
                isLocked = true
                try {
                    startLockTask()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Не удалось включить закрепление экрана",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        @JavascriptInterface
        fun unlock() {
            runOnUiThread {
                isLocked = false
                try {
                    stopLockTask()
                } catch (e: Exception) { }
            }
        }
    }

    override fun onBackPressed() {
        if (isLocked) {
            return
        }
        super.onBackPressed()
    }
}
