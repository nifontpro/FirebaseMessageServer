package ru.nifontbus.firebasemessageserver.okhttp

import android.util.Log
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.nifontbus.firebasemessageserver.App
import ru.nifontbus.firebasemessageserver.BuildConfig
import java.io.IOException

class OkHtmlRepository {

    /*
    https://askdev.ru/q/kak-otpravit-soobscheniya-ustroystva-na-ustroystvo-s-pomoschyu-firebase-cloud-messaging-45452/
    https://stackoverflow.com/questions/56893945/how-to-use-okhttp-to-make-a-post-request-in-kotlin

    Формат HTTP - POST запроса
    Content-Type:application/json
    Authorization:key=AIzaSyZ-1u...0GBYzPu7Udno5aA

        {
            "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
            "data" : {
            ...
        },
    }*/

    /**
     * Отправка push-сообщений
     * LEGACY_SERVER_KEY - ключ приложения в настройках Firebase
     * REG_TOKEN - токен, хранящийся локально на устройстве.
     */
    fun sendPush(title: String, body: String) {
        App.pushHandler.post {
            val client = OkHttpClient()
            val json = JSONObject()
            val dataJson = JSONObject()

            dataJson.put("body", body)
            dataJson.put("title", title)
            json.put("to", BuildConfig.REG_TOKEN)
            json.put("notification", dataJson)

            Log.d(App.TAG, "json: $json")

            val request = Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .header("Content-Type", "application/json")
                .header("Authorization", "key=${BuildConfig.LEGACY_SERVER_KEY}")
                .method("POST", json.toString().toRequestBody())
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d(App.TAG, "Push send Ok")
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e(App.TAG, "Error send push message!")
                }
            })
        }
    }
}