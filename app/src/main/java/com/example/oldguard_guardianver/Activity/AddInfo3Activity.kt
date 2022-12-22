package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.contact.AddInfoRequest
import com.example.oldguard_guardianver.Request.guest.GuestListResponse
import com.example.oldguard_guardianver.databinding.ActivityAddInfo3Binding
import com.example.oldguard_guardianver.intent.PatternIntent
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**   세번째 어르신 추가 화면   */
class AddInfo3Activity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAddInfo3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //activity_add_info3와 연결
        viewBinding = ActivityAddInfo3Binding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val extras = intent.extras

        var gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${App.token_prefs.accessToken}")
                .build()
            chain.proceed(newRequest)
        }.build()
        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        var server = retrofit.create(HowIService::class.java)
        var guestResponseList = mutableListOf<GuestListResponse>()
        var list_size = 0;

        server.getResponse().enqueue(object : Callback<List<GuestListResponse>> {
            override fun onFailure(call: Call<List<GuestListResponse>>, t: Throwable) {
                Log.e("실패",t.toString())
            }
            override fun onResponse(call: Call<List<GuestListResponse>>, response: Response<List<GuestListResponse>>) {
                Log.d("getResponse()성공", response.body().toString())
                if (response.body() != null) {
                    for (index in response.body()!!.indices)
                        guestResponseList.add(response.body()?.get(index)!!)
                    Log.d("guestResponseList1", guestResponseList.toString())

                    list_size = guestResponseList.size
                    Log.d("guestResponseList.size",list_size.toString())
                }
            }
        })
        //다음단계 버튼 눌렀을 때
        viewBinding.nextBtn.setOnClickListener() {
            var name = viewBinding.editGuardianName3.text.toString()
            var number = viewBinding.editGuardianNumber3.text.toString()
            var request = AddInfoRequest(name,list_size.toLong()-1, number)
            var gson = GsonBuilder().setLenient().create()


            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${App.token_prefs.accessToken}")
                    .build()
                chain.proceed(newRequest)
            }.build()

            var retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            var server = retrofit.create(HowIService::class.java)
            server.postAddInfoRequest(request).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("성공",response.body().toString())
                }
            })
            val intent = Intent(this, PatternIntent::class.java)
            startActivity(intent)
        }
    }
}