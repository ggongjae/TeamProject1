package com.example.oldguard_guardianver.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oldguard_guardianver.Request.guest.GuestLoginRequest
import com.example.oldguard_guardianver.databinding.ItemElderManagerBinding

/**   메인화면(ElderManagerActivity)의 RecyclervView를 위한 어댑터   */
class ElderManagerRVAdapter (private var dataList : ArrayList<GuestLoginRequest>) :
    RecyclerView.Adapter<ElderManagerRVAdapter.ItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(view : View, data : GuestLoginRequest, position: Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    interface OnBtnClickListener {
        fun onBtnClick(view : View, data : GuestLoginRequest, position : Int)
    }
    private var btnListener : OnBtnClickListener? = null
    fun setOnBtnClickListener(btnListener : OnBtnClickListener) {
        this.btnListener = btnListener
    }

    inner class ItemViewHolder(private val viewBinding : ItemElderManagerBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data : GuestLoginRequest, position: Int, holder : ItemViewHolder) {
            viewBinding.elderName.text = data.name

            //삭제 버튼 누르면 화면에서 삭제되게 함
            viewBinding.elderDeleteBtn.setOnClickListener {
                btnListener?.onBtnClick(holder.itemView, dataList[position], position)
            }
        }
    }

    //RecycleView 재사용 오류 방지 코드
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val viewBinding = ItemElderManagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], position, holder)
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, dataList[position], position)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

}