package com.example.oldguard_guardianver.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.R
import com.example.oldguard_guardianver.databinding.ActivityLoginBinding
import com.google.gson.GsonBuilder
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**   첫 화면 카카오톡/네이버 로그인 화면   */
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null  //activity_login 화면과 연결
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** HashKey확인 */
        val keyHash = Utility.getKeyHash(this)
        TextMsg(this, "HashKey: ${keyHash}")

        /** KakoSDK init */
        KakaoSdk.init(this, this.getString(R.string.kakao_app_key))
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                TextMsg(this, "카카오계정으로 로그인 실패 : ${error}")
                setLogin(false)
            } else if (token != null) {
                var gson = GsonBuilder().setLenient().create()
                var retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                var server = retrofit.create(HowIService::class.java)
                server.postLoginRequest(token.accessToken).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val result = response.body()
                        Log.d("로그인", result.toString())
                        //데이터 저장
                        if (response.isSuccessful && response.body() != null) {
                            App.token_prefs.accessToken = response.body()
                        }

                    }

                })
            }
            val intent = Intent(this, ElderlyManagerActivity::class.java)
            startActivity(intent)
        }
        /** Click_listener */
        binding.mainKakaoLogin.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        TextMsg(this, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        setLogin(true)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
    private fun TextMsg(act: Activity, msg : String){
        binding.tvHashKey.text = msg
    }
    private fun setLogin(bool: Boolean){
        binding.mainKakaoLogin.visibility = if(bool) View.VISIBLE else View.VISIBLE
    }
}