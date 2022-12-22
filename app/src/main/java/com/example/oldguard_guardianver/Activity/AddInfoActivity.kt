package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.*
import com.example.oldguard_guardianver.Request.contact.AddInfoRequest
import com.example.oldguard_guardianver.Request.contact.ContactResponse
import com.example.oldguard_guardianver.Request.guest.GuestListResponse
import com.example.oldguard_guardianver.Request.guest.GuestLoginRequest
import com.example.oldguard_guardianver.databinding.ActivityAddInfoBinding
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

/**   첫번째 어르신 추가 화면   */
class AddInfoActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAddInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //activity_add_info와 연결
        viewBinding = ActivityAddInfoBinding.inflate(layoutInflater)
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

        server.getContacts(list_size.toLong()-1).enqueue(object : Callback <List<ContactResponse>>{
            override fun onFailure(call: Call <List<ContactResponse>>, t: Throwable) {
                Log.e("실패",t.toString())
            }
            override fun onResponse(call: Call<List<ContactResponse>>, response: Response<List<ContactResponse>>) {
                Log.d("성공", response.body().toString())
            }
        })

        //연락처 추가하기 버튼 눌렀을 때
        viewBinding.addNumberBtn.setOnClickListener {
            var name = viewBinding.editGuardianName1.text.toString()
            var number = viewBinding.editGuardianNumber1.text.toString()
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

            var request = AddInfoRequest(name,list_size.toLong()-1, number)
            server.postAddInfoRequest(request).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("성공",response.body().toString())
                }
            })
            server.getContacts(0L).enqueue(object : Callback <List<ContactResponse>>{
                override fun onFailure(call: Call <List<ContactResponse>>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<List<ContactResponse>>, response: Response<List<ContactResponse>>) {
                    Log.d("성공", response.body().toString())
//                    nameTemp = response.body()?.get(0)?.name.toString()
//                    Log.d("성공", nameTemp)
//                    numberTemp = response.body()?.get(0)?.contact.toString()
//                    Log.d("성공", numberTemp)
                }
            })
            val intent = Intent(this, AddInfo2Activity::class.java)
            startActivity(intent)
        }

        //다음단계 버튼 눌렀을 때
        viewBinding.nextBtn.setOnClickListener() {
            var name = viewBinding.editGuardianName1.text.toString()
            var number = viewBinding.editGuardianNumber1.text.toString()
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
            var request = AddInfoRequest(name,list_size.toLong()-1, number)
            server.postAddInfoRequest(request).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("성공",response.body().toString())
                }
            })
            server.getContacts(0L).enqueue(object : Callback <List<ContactResponse>>{
                override fun onFailure(call: Call <List<ContactResponse>>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<List<ContactResponse>>, response: Response<List<ContactResponse>>) {
                    Log.d("성공", response.body().toString())
//                    nameTemp = response.body()?.get(0)?.name.toString()
//                    Log.d("성공", nameTemp)
//                    numberTemp = response.body()?.get(0)?.contact.toString()
//                    Log.d("성공", numberTemp)
                }
            })
            server.getResponse().enqueue(object : Callback <List<GuestListResponse>> {
                override fun onFailure(call: Call<List<GuestListResponse>>, t: Throwable) {
                    Log.e("실패",t.toString())
                }
                override fun onResponse(call: Call<List<GuestListResponse>>, response: Response<List<GuestListResponse>>) {
                    Log.d("성공", response.body().toString())
                    if (response.body() != null) {
                        Log.d("id=1", "is not null")
                        var request_1 = AddInfoRequest(name, 1L, number)
                        server.postAddInfoRequest(request_1).enqueue(object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("실패",t.toString())
                            }
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                Log.d("성공",response.body().toString())
                            }
                        })
                    }
                }
            })
            val intent = Intent(this, PatternIntent::class.java)
            startActivity(intent)
        }
    }
}