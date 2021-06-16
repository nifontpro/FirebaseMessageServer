package ru.nifontbus.firebasemessageserver

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import android.util.Log

class App: Application() {
    companion object {
        const val TAG = "My log"
        private val handlerThread by lazy { HandlerThread("pushThread") }
        val pushHandler by lazy { Handler(handlerThread.looper) }
        init {
            handlerThread.start()
            Log.e(TAG, "Start Push Thread!")
        }
    }

    override fun onTerminate() {
        handlerThread.quitSafely()
        super.onTerminate()
    }
}