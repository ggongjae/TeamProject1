package com.example.oldguard_guardianver.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.Contact
import com.example.oldguard_guardianver.Request.GuestResponse
import com.example.oldguard_guardianver.Request.contact.ContactResponse
import com.example.oldguard_guardianver.Request.contact.DeleteContactRequest
import com.example.oldguard_guardianver.Request.contact.EditContactRequest
import com.example.oldguard_guardianver.Request.guest.GuestListResponse
import com.example.oldguard_guardianver.databinding.ActivityGuardianCorrectionBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.properties.Delegates

/**   어르신 관리에서 보호자 정보 수정 화면(삭제 저장 가능)   */
class GuardianCorrectionActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityGuardianCorrectionBinding //activity_guardian_correction화면과 연결
    private lateinit var guestID: Integer

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        var position = intent.getIntExtra("position",0) //꼭 위에 위치시키기
//        Log.d("position", position.toString())
        var intent1 : Intent

        var data = intent

        viewBinding = ActivityGuardianCorrectionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        //삭제 버튼 눌렀을 때
        viewBinding.deleteBtn.setOnClickListener {
            //삭제 확인 창
            val builder = AlertDialog.Builder(this)
                .setTitle("경고")
                .setTitle("보호자 정보를 삭제하시겠습니까?\n")
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        //아무런 행동 없음.
                    })
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        intent1 = Intent(this, ElderInfoActivity::class.java)
                        intent1.putExtra("position", position)
                        setResult(DELETE, intent)

                        var request1 = DeleteContactRequest(position.toLong(), 0L)
                        var gson = GsonBuilder().setLenient().create()

                        val client = OkHttpClient.Builder().addInterceptor { chain ->
                            val newRequest: Request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer ${App.token_prefs.accessToken}")
                                .build()
                            chain.proceed(newRequest)
                        }.build()

                        var retrofit1 = Retrofit.Builder()
                            .client(client)
                            .baseUrl("http://10.0.2.2:8080")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build()
                        var server = retrofit1.create(HowIService::class.java)
                        server.deleteContact(request1).enqueue(object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("삭제 실패", "${t.localizedMessage}")
                            }
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                Log.d("삭제 성공",response.body().toString())
                            }
                        })

                        finish()    //Activity 종료
                    })
            builder.show()
        }

        //저장 버튼 눌렀을 때
        viewBinding.saveBtn.setOnClickListener {
            Log.d("editGuestName1", data.getStringExtra("guestName").toString())
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

//            var name = viewBinding.editCorrectName.text.toString()
//            var contact = viewBinding.editCorrectNumber.text.toString()
//
//            Log.d("position", position.toString())
//            var request = EditContactRequest(contact,position.toLong(),name)
//            server.EditContact(request).enqueue(object : Callback<String> {
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Log.e("어르신 수정 실패", "${t.localizedMessage}")
//                }
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    Log.d("수정성공",response.body().toString())
//                }
//            })

            var name = viewBinding.editCorrectName.text.toString()
            var contact = viewBinding.editCorrectNumber.text.toString()
            var request = EditContactRequest(contact,1L,name)
            server.EditContact(request).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("어르신 수정 실패", "${t.localizedMessage}")
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("수정성공",response.body().toString())
                }
            })

            val intent = Intent(this, ElderlyManagerActivity::class.java)
            intent.putExtra("position", position)
            setResult(FIX, intent)
            startActivity(intent)
//            finish()
        }
    }
    companion object{
        const val DELETE = 1001 //삭제했을 때 resultCode
        const val FIX = 1002    //수정했을 때 resultCode
    }
}