package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.Adapter.RecordVPAdapter
import com.example.oldguard_guardianver.databinding.ActivityRecordBinding
import com.google.android.material.tabs.TabLayoutMediator

/**   기록을 보여주는 화면(ReceivedRecordFragment와 DeletedRecordFragment가 들어감)   **/
class RecordActivity : AppCompatActivity() {
    private val viewBinding : ActivityRecordBinding by lazy {
        ActivityRecordBinding.inflate(layoutInflater)
    }
    var isChanged : Boolean = false //데이터에 변경이 일어났는지 알아보는 불린 함수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val recordVPAdapter = RecordVPAdapter(this)

        //어댑터와 실제 객체 연결
        viewBinding.vpRecord.adapter = recordVPAdapter

        //tab 제목에 대한 배열
        val tabTitleArray = arrayOf(
            "받은 알람", "삭제한 어르신"
        )

        //클래스에 tab 간편 연결
        TabLayoutMediator(viewBinding.tabRecord, viewBinding.vpRecord)
        { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        //이전 activity로 가는 back 버튼 눌렀을 떄
        viewBinding.backBtn.setOnClickListener {
            val intent = Intent(this, ElderlyManagerActivity::class.java)

            //어르신 복구 등 변경이 일어나지 않고 보기만 했을 때
            if(isChanged === false) {
                setResult(READ, intent)
                finish()    //activity 종료
            }
            //어르신 복구 등 변경이 일어났을 때
            else {
                //dataClass 전달
                //intent.putExtra("key",dataclass) 예시
                setResult(WRITE, intent)
                finish()
            }
        }
    }
    companion object{
        const val READ = 1001
        const val WRITE = 1002
    }
}