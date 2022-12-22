package com.example.oldguard_guardianver.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.databinding.ActivityDegAndWindowCorrectionBinding

/**   어르신 관리에서 온도, 창문열림 시간 수정 화면   */
class DegAndWindowCorrectionActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDegAndWindowCorrectionBinding     //activity_deg_and_window_correction과 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent: Intent
        viewBinding = ActivityDegAndWindowCorrectionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //저장 버튼을 눌렀을 때
        viewBinding.saveBtn.setOnClickListener {
            var startDeg = viewBinding.editStartDeg.toString()
            var endDeg = viewBinding.editEndDeg.toString()
            var windowTime = viewBinding.editWindowTime.toString()

            //레트로핏 사용하는 코드 써주세요

            val intent = Intent(this, ElderInfoActivity::class.java)
            setResult(FIX, intent)
            finish()
        }
    }

    companion object{
        const val FIX = 1002    //수정했을 때 resultCode
    }
}