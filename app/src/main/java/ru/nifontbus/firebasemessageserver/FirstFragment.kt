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
import ru.nifontbus.firebasemessageserver.App.Companion.TAG
import ru.nifontbus.firebasemessageserver.App.Companion.pushHandler
import ru.nifontbus.firebasemessageserver.databinding.FragmentFirstBinding
import ru.nifontbus.firebasemessageserver.okhttp.OkHtmlRepository
import ru.nifontbus.firebasemessageserver.retrofit.RetrofitRepository

class FirstFragment : Fragment() {

    private  val repository by lazy { OkHtmlRepository() }
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

        binding.fab.setOnClickListener {
            repository.sendPush(binding.etBody.text.toString(), binding.etTitle.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}