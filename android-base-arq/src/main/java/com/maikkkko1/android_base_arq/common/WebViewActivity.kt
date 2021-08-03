package com.maikkkko1.android_base_arq.common

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private var mWebview: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWebview = WebView(this)

        mWebview!!.settings.javaScriptEnabled = true // Enable JS.

        val activity: Activity = this

        mWebview!!.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
            ) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                    view: WebView,
                    req: WebResourceRequest,
                    rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                        view,
                        rerr.errorCode,
                        rerr.description.toString(),
                        req.url.toString()
                )
            }
        }

        mWebview!!.loadUrl(intent.getStringExtra("targetUrl") ?: "https://google.com")

        setContentView(mWebview)
    }
}