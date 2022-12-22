package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.guest.EditGuestInfoRequest
import com.example.oldguard_guardianver.Request.guest.GuestListResponse
import com.example.oldguard_guardianver.databinding.ActivityPatternPhachBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalTime

/**   어르신 관리에서 생활패턴 수정 화면   */
class PartternPhachActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPatternPhachBinding   //activity_patten_phach 화면과 연결

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPatternPhachBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var data = intent

        //저장 버튼을 눌렀을 때
        viewBinding.saveBtn.setOnClickListener {
            var callTime = viewBinding.elderCallTime.text.toString().toLong()
            var emergencyTime = viewBinding.elderSosTime.text.toString().toLong()

            var messageTime = viewBinding.elderMessageTime.text.toString().toLong()
            var sleepEndTime: LocalTime = LocalTime.of(viewBinding.elderEndHour.text.toString().toInt(),viewBinding.elderEndMinute.text.toString().toInt())
            var sleepStartTime: LocalTime = LocalTime.of(viewBinding.elderStartHour.text.toString().toInt(),viewBinding.elderStartMinute.text.toString().toInt())

            //레트로핏 사용하는 코드 써주세요
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
            var GuestID = -1L

            server.getResponse().enqueue(object : Callback<List<GuestListResponse>> {
                override fun onFailure(call: Call<List<GuestListResponse>>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<List<GuestListResponse>>, response: Response<List<GuestListResponse>>) {
                    Log.d("getResponse()성공", response.body().toString())
                    for (index in response.body()!!.indices)
                        guestResponseList.add(response.body()?.get(index)!!)
                    for (index in guestResponseList.indices) {
                        if (guestResponseList[index].name.equals(data.getStringExtra("guestName"))) {
                            GuestID = guestResponseList[index].id
                            Log.d("GuestID",GuestID.toString())
                        }
                    }

                    var request = EditGuestInfoRequest(callTime,emergencyTime ,GuestID, messageTime, sleepEndTime.toString(), sleepStartTime.toString())
                    server.EditGuestInfo(request).enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("어르신 정보 수정 실패", "${t.localizedMessage}")
                        }
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            Log.d("성공",response.body().toString())
                        }
                    })
                }
            })

            val intent = Intent(this, ElderlyManagerActivity::class.java)
            setResult(FIX, intent)
            startActivity(intent)
//            finish()
        }
    }

    companion object{
        const val FIX = 1002    //수정했을 때 resultCode
    }
}