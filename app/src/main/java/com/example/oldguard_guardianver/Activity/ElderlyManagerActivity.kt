package com.example.oldguard_guardianver.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oldguard_guardianver.Adapter.ElderManagerRVAdapter
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.*
import com.example.oldguard_guardianver.Request.contact.AddInfoRequest
import com.example.oldguard_guardianver.Request.guest.DeleteGuestInfoRequest
import com.example.oldguard_guardianver.Request.guest.GuestListResponse
import com.example.oldguard_guardianver.Request.guest.GuestLoginRequest
import com.example.oldguard_guardianver.databinding.ActivityElderlyManagerBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.Serializable

/**   관리하는 어르신을 보여주는 메인 화면   */
class ElderlyManagerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityElderlyManagerBinding //activity_elder_manager과 연결
    private lateinit var getResultText : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityElderlyManagerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val dataList : ArrayList<GuestLoginRequest> = arrayListOf()

        val elderManagerRVAdapter = ElderManagerRVAdapter(dataList)
        viewBinding.elderManagerRv.adapter = elderManagerRVAdapter
        //역순으로 (생성된 순) 출력
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        manager.stackFromEnd = true
        //LayoutManager 설정
        viewBinding.elderManagerRv.layoutManager = manager

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

        var contactlist = mutableListOf<Contact>()

        var guestResponseList = mutableListOf<GuestListResponse>()

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

                    for (index in guestResponseList.indices)
                        dataList.apply {
                            add(GuestLoginRequest("ABCDEF","address",guestResponseList[index].name))
                        }
                    elderManagerRVAdapter.notifyItemInserted(elderManagerRVAdapter.itemCount)
                    if(dataList.isNotEmpty()) {
                        viewBinding.letsStartText.visibility = View.INVISIBLE
                    }
                }
            }
        })

        //추가하기 버튼 눌렀을 때
        viewBinding.addBtn.setOnClickListener {
            val intent = Intent(this, AuthCodeActivity::class.java)
            startActivity(intent)
        }

        //기록 버튼을 눌렀을 때
        viewBinding.recordBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            getResultText.launch(intent)
        }

        //기록 버튼을 눌렀을 떄는 intent.이 아니라 result.으로 전달값을 받는다
        //이 범위 안에서만
        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
                result ->
            //기록 activity를 읽기만 했을 때
            if (result.resultCode == READ) {
                //화면만 돌아옴 따로 추가할 내용 X
            }
            //기록 activity를 읽고 삭제된 목록을 복구했을 떄
            else if (result.resultCode == WRITE) {
                //RecyclerView add 사용해서 추가
            } else {
                Log.d("에러메시지", "resultCode값 없음")
            }
        }

        //dataList가 비어있지 않으면 text 보이지 않게 하기
        if(dataList.isNotEmpty()) {
            viewBinding.letsStartText.visibility = View.INVISIBLE
        }
        var intent : Intent

        //탈퇴하기 버튼 눌렀을 때
        viewBinding.withdraw.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
                .setTitle("경고")
                .setTitle("어르신 정보를 삭제하시겠습니까?\n삭제 기록에서 복구할 수 있습니다.\n")
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        //아무런 행동 없음.
                    })
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which ->
                        //여기에 전체 delete하는 기능 구현하기
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
                        var server1 = retrofit.create(HowIService::class.java)

                        server1.userMembershipWithdrawal().enqueue(object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("실패",t.toString())
                            }
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                Log.d("Withdrawal성공", response.body().toString())
                            }
                        })
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
//                        finish()    //Activity 종료
                    })
            builder.show()
        }

        //아이템 전체를 눌렀을 떄
        elderManagerRVAdapter.setOnItemClickListener(object : ElderManagerRVAdapter.OnItemClickListener {
            override fun onItemClick(view : View, data : GuestLoginRequest, position : Int) {
                intent = Intent(this@ElderlyManagerActivity, ElderInfoActivity::class.java)
                intent.putExtra("guestName", data.name.toString())

                startActivity(intent)
            }
        })

        //아이템에서 쓰레기통 버튼을 눌렀을 때
        elderManagerRVAdapter.setOnBtnClickListener(object : ElderManagerRVAdapter.OnBtnClickListener {
            override fun onBtnClick(view: View, data: GuestLoginRequest, position: Int) {
                val builder = AlertDialog.Builder(this@ElderlyManagerActivity)
                    .setTitle("경고")
                    .setTitle("어르신 정보를 삭제하시겠습니까?\n삭제 기록에서 복구할 수 있습니다.\n")
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, which ->
                            //아무 활동도 하지 않음. 코드 작성 필요X
                        })
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            dataList.removeAt(position)
                            elderManagerRVAdapter.notifyItemRemoved(position)
                            var GuestID = 0L

                            for (index in guestResponseList.indices) {
                                if (guestResponseList[index].name.equals(data.name)) {
                                    GuestID = guestResponseList[index].id
                                }
                            }
                            var request1 = DeleteGuestInfoRequest(GuestID)
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

                            server.deleteGuestInfo(request1).enqueue(object : Callback<String> {
                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.e("실패",t.toString())
                                }
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    Log.d("성공", response.body().toString())
                                }
                            })
                        })
                builder.show()
            }
         })
    }
    companion object{
        const val READ = 1001
        const val WRITE = 1002
    }
}