package com.example.oldguard_guardianver.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oldguard_guardianver.Adapter.DeletedRecordRVAdapter
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.record.DeleteRecordResponse
import com.example.oldguard_guardianver.Request.record.RecordRequest
import com.example.oldguard_guardianver.databinding.FragmentDeletedRecordBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**   기록에서 삭제했던 어르신 보기 및 복구 화면   */
class DeleteRecordFragment : Fragment() {
    lateinit var viewBinding : FragmentDeletedRecordBinding
    private lateinit var adapter : DeletedRecordRVAdapter
    val manager = LinearLayoutManager(activity)
    val dataList : ArrayList<DeleteRecordResponse> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_deleted_record 와 연결
        viewBinding = FragmentDeletedRecordBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = ArrayList<DeleteRecordResponse>()
        val deletedRecordRVAdapter = DeletedRecordRVAdapter(dataList)

        adapter = DeletedRecordRVAdapter(dataList)
        viewBinding.deletedRv.adapter = adapter

//      처음 시작은 역순으로 (최신순) 출력
        manager.reverseLayout = true
        manager.stackFromEnd = true
        viewBinding.deletedRv.layoutManager = manager

        //삭제된 정보를 받아온다.
        var request = RecordRequest("sort")
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
        var deleteRecordList = mutableListOf<DeleteRecordResponse>()
        var server = retrofit.create(HowIService::class.java)
        server.getDeleteRecord(request).enqueue(object : Callback<List<DeleteRecordResponse>> {
            override fun onFailure(call: Call<List<DeleteRecordResponse>>, t: Throwable) {
                Log.e("실패",t.toString())
            }
            override fun onResponse(call: Call<List<DeleteRecordResponse>>, response: Response<List<DeleteRecordResponse>>) {
                Log.d("성공", response.body().toString())
                for(index in response.body()!!.indices){
                    deleteRecordList.add(response.body()?.get(index)!!)
                }

                for (index in deleteRecordList.indices)
                    dataList.apply {
                        add(DeleteRecordResponse(deleteRecordList[index].localDateTime.substring(0,10),deleteRecordList[index].name))
                    }
                deletedRecordRVAdapter.notifyItemInserted(deletedRecordRVAdapter.itemCount)
            }
        })

        //드롭다운 spinner 구현화면
        viewBinding.deletedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (viewBinding.deletedSpinner.getItemAtPosition(position)) {
                    "최신순" -> {
                        updateLatestDeleted()
                    }
                    "오래된순" -> {
                        updateOldDeleted()
                    }
                    else -> {
                        updateLatestDeleted()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //최신순으로 add
    fun updateLatestDeleted() {
        //역순으로 recyclerView 정렬
        manager.reverseLayout = true
        manager.stackFromEnd = true
        viewBinding.deletedRv.layoutManager = manager
    }
    //오래된 순으로 add
    fun updateOldDeleted() {
        //원래대로 recyclerView 정렬
        manager.reverseLayout = false
        manager.stackFromEnd = false
        viewBinding.deletedRv.layoutManager = manager
    }


}