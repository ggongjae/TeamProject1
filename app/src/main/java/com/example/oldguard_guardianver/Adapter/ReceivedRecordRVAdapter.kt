package com.example.oldguard_guardianver.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oldguard_guardianver.Request.ReceivedRecordData
import com.example.oldguard_guardianver.databinding.ItemReceivedDataBinding

/**   왔던 알람 기록(ReceivedRecordActivity)의 recyclerView를 위한 어댑터   */
class ReceivedRecordRVAdapter (private var dataList : ArrayList<ReceivedRecordData>) :
    RecyclerView.Adapter<ReceivedRecordRVAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val viewBinding : ItemReceivedDataBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data : ReceivedRecordData, position: Int) {
            viewBinding.receivedElderName.text = data.name
            viewBinding.receivedTime.text = data.time
            viewBinding.receivedType.text = data.type
        }
    }

    //RecycleView 재사용 오류 방지 코드
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val viewBinding = ItemReceivedDataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int = dataList.size


}