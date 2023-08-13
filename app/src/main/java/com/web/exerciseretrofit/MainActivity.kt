package com.web.exerciseretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.web.exerciseretrofit.databinding.ActivityMainBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var service : Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        service = RetrofitConfig().getClient().create(Service::class.java)

        binding.btGerar.setOnClickListener {
            val call: Call<ResponseBody> = service.getData()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody: ResponseBody? = response.body()
                        val content: String = responseBody?.string() ?: ""

                        val gson = Gson()
                        try {
                            val responseData = gson.fromJson(content, ResponseClass::class.java)
                            val dataArray = responseData.data
                            val text = dataArray[0].toString()
                            if (dataArray.isNotEmpty()) {
                                binding.tvFact.text = text
                            } else {
                                binding.tvFact.text = "No 'text' found in response"
                            }
                        } catch (e: Exception) {
                            binding.tvFact.text = "JSON parsing error"
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Erro ao obter resposta", Toast.LENGTH_SHORT)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Network Failure", Toast.LENGTH_SHORT)
                }
            })
        }
    }
}