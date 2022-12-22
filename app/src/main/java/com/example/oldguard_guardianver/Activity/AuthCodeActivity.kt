package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.Request.guest.GuestLoginRequest
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.databinding.ActivityAuthCodeBinding
import com.example.oldguard_guardianver.intent.ContactIntent
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**   어르신 추가 시, 어르신 성함, 코드 주소 입력받는 화면   */
class AuthCodeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAuthCodeBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        //activity_auth_code와 연결
        viewBinding = ActivityAuthCodeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //다음 버튼 눌렀을 때
        viewBinding.nextBtn.setOnClickListener {
                                                                    //,viewBinding.editGuardianAddress.text.toString() -> 데이터 받는 값에 주소지 추가 시 사용될 매개변수
            var request = GuestLoginRequest(viewBinding.editLoginCode.text.toString(),viewBinding.editGuardianAddress.text.toString(),viewBinding.editElderName.text.toString())
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
            server.postGuestLoginRequest(request).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("실패", t.toString())
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("성공",response.body().toString())
                }
            })
            val intent = Intent(this, ContactIntent::class.java)
            intent.flags  = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }
}