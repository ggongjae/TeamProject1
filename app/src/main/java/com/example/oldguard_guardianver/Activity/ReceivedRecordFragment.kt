package com.example.oldguard_guardianver.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oldguard_guardianver.Adapter.ReceivedRecordRVAdapter
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.ReceivedRecordData
import com.example.oldguard_guardianver.Request.record.RecordRequest
import com.example.oldguard_guardianver.Request.record.SendRecordResponse
import com.example.oldguard_guardianver.databinding.FragmentReceivedRecordBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**   기록에서 받은 알림 기록 화면   */
class ReceivedRecordFragment : Fragment() {
    lateinit var viewBinding : FragmentReceivedRecordBinding    //fragment_received_record화면과 연결
    private lateinit var adapter : ReceivedRecordRVAdapter
    val manager = LinearLayoutManager(activity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentReceivedRecordBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = listOf<String>("최신순","오래된순")
        val dataList = ArrayList<ReceivedRecordData>()
        adapter = ReceivedRecordRVAdapter(dataList)
        viewBinding.receivedRv.adapter = adapter

//      처음 시작은 역순으로 (최신순) 출력
        manager.reverseLayout = true
        manager.stackFromEnd = true
        viewBinding.receivedRv.layoutManager = manager


        //확인용 데이터들. 실제 구현할 떄 삭제해도돼요
        dataList.apply {
            add(ReceivedRecordData("박화자","2022.11.22 22:15","문자"))
            add(ReceivedRecordData("김성진","2022.11.22 22:16","전화"))
            add(ReceivedRecordData("박화자","2022.11.22 22:17","문자"))
            add(ReceivedRecordData("이영자","2022.11.22 22:18","문자"))
            add(ReceivedRecordData("김미정","2022.11.22 22:19","문자"))
            add(ReceivedRecordData("최정숙","2022.11.22 22:20","문자"))
            add(ReceivedRecordData("김영호","2022.11.22 22:21","문자"))
            add(ReceivedRecordData("신영식","2022.11.22 22:22","문자"))
            add(ReceivedRecordData("박영길","2022.11.22 22:23","전화"))
            add(ReceivedRecordData("김정수","2022.11.22 22:24","전화"))
        }
//                새로운 데이터 추가 시 add, 수정 시 set, 삭제 시 removeAt
//                adapter.items = dataList
//                adapter.notifyDataSetChanged()

                viewBinding.receivedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (viewBinding.receivedSpinner.getItemAtPosition(position)) {
                    "최신순" -> {
                        updateLatestReceived()
                    }
                    "오래된순" -> {
                        updateOldReceived()
                    }
                    else -> {
                        updateLatestReceived()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //최신순으로 add
     fun updateLatestReceived() {
        manager.reverseLayout = true
        manager.stackFromEnd = true
        viewBinding.receivedRv.layoutManager = manager
     }
    //오래된 순으로 add
    fun updateOldReceived() {
        manager.reverseLayout = false
        manager.stackFromEnd = false
        viewBinding.receivedRv.layoutManager = manager
    }
}