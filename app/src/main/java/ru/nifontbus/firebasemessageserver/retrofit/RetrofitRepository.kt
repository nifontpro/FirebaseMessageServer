package ru.nifontbus.firebasemessageserver.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nifontbus.firebasemessageserver.App.Companion.TAG
import ru.nifontbus.firebasemessageserver.BuildConfig

class RetrofitRepository {

    private val firebaseAPI = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com")
//        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FirebaseAPI::class.java)

    fun sendPush(title: String, body: String) {

        //for messaging server
        val notifyData = NotifyData(title, body)
        val call = firebaseAPI.sendMessage(Message(BuildConfig.REG_TOKEN, notifyData))

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                Log.d(TAG, "onResponse push message from retrofit")
            }
            override fun onFailure(call: Call<Message>?, t: Throwable) {
                Log.e(TAG, "onFailure push message from retrofit")
            }
        })
    }

}