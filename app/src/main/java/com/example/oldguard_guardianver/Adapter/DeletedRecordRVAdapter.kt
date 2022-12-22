package com.example.oldguard_guardianver.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oldguard_guardianver.App
import com.example.oldguard_guardianver.HowIService
import com.example.oldguard_guardianver.Request.guest.GuestRestorationRequest
import com.example.oldguard_guardianver.Request.record.DeleteRecordResponse
import com.example.oldguard_guardianver.databinding.ItemDeletedDataBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**   삭제했던 어르신 기록(DeletedRecordActivity)의 RecyclerView에서 사용할 어댑터   */
class DeletedRecordRVAdapter (private var dataList : ArrayList<DeleteRecordResponse>) : RecyclerView.Adapter<DeletedRecordRVAdapter.ItemViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(view : View, data : DeleteRecordResponse, position: Int)
    }
    private var btnListener : OnBtnClickListener? = null
    fun setOnBtnClickListener(btnListener: OnBtnClickListener) {
        this.btnListener = btnListener
    }

    inner class ItemViewHolder(private val viewBinding : ItemDeletedDataBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: DeleteRecordResponse, position: Int, holder: ItemViewHolder) {
            viewBinding.deletedElderName.text = data.name
            viewBinding.deletedTime.text = data.localDateTime

            viewBinding.restoreBtn.setOnClickListener {
                dataList.removeAt(position)
                notifyItemRemoved(position)
                btnListener?.onBtnClick(holder.itemView, dataList[position], position)
                var request = GuestRestorationRequest(0L)
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
                server.restorationGuest(request).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("실패",t.toString())
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("복구성공", response.body().toString())
                    }
                })
            }
        }
    }

    //RecycleView 재사용 오류 방지 코드
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val viewBinding = ItemDeletedDataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], position, holder)
    }

    override fun getItemCount(): Int = dataList.size


}