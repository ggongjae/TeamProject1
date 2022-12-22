package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.databinding.ActivityDegAndWindowBinding
import com.example.oldguard_guardianver.intent.MainIntent

/**   어르신 추가 시 온도, 창문열림 설정 화면   */
class DegAndWindowActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDegAndWindowBinding   //activity_deg_and_window와 연결

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityDegAndWindowBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //저장 버튼을 눌렀을 때
        viewBinding.nextBtn.setOnClickListener() {
            val intent = Intent(this, MainIntent::class.java)
            startActivity(intent)
        }
    }
}