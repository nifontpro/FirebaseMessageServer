package ru.nifontbus.firebasemessageserver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import ru.nifontbus.firebasemessageserver.App.Companion.pushHandler
import ru.nifontbus.firebasemessageserver.databinding.FragmentFirstBinding

const val TAG = "My log"

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.button.setOnClickListener {
            sendPush(binding.etBody.text.toString(), binding.etTitle.text.toString()) }
    }

/*  Формат HTTP - POST запроса
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
     *
     */
    private fun sendPush(title: String, body: String) {
        pushHandler.post {
            val client = OkHttpClient()
            val json = JSONObject()
            val dataJson = JSONObject()

            dataJson.put("body", body)
            dataJson.put("title", title)
            json.put("to", BuildConfig.REG_TOKEN)
            json.put("notification", dataJson)

            Log.d("my", "json: $json")

            val request = Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .header("Content-Type", "application/json")
                .header("Authorization", "key=${BuildConfig.LEGACY_SERVER_KEY}")
                .method("POST", json.toString().toRequestBody())
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("my", "Push send Ok")
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("my", "Error send push message!")
                }
            })
        }
    }

    //        https://stackoverflow.com/questions/56893945/how-to-use-okhttp-to-make-a-post-request-in-kotlin

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}