package com.example.oldguard_guardianver.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oldguard_guardianver.Request.contact.AddInfoRequest
import com.example.oldguard_guardianver.databinding.ItemElderInfoBinding

/**   메인에서 선택한 어르신의 보호자관리(ElderInfoActivity)의 ReyclerView에서 사용하는 어댑터   */
class ElderInfoRVAdapter (private val datalist : ArrayList<AddInfoRequest>) : RecyclerView.Adapter<ElderInfoRVAdapter.DataViewHolder>() {

    interface OnBtnClickListener {
        fun onBtnClick(view : View, data : AddInfoRequest, position: Int)
    }
    private var btnListener : OnBtnClickListener? = null
    fun setOnBtnClickListener(btnListener: OnBtnClickListener) {
        this.btnListener = btnListener
    }

    inner class DataViewHolder(private val viewBinding : ItemElderInfoBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: AddInfoRequest, position: Int, holder: DataViewHolder) {
            viewBinding.guardianName1.text = data.name
            viewBinding.guardianNumber1.text = data.number

            viewBinding.guardianBtn1.setOnClickListener {
                btnListener?.onBtnClick(holder.itemView, datalist[position], position)
            }
        }
    }

    //RecycleView 재사용 오류 방지 코드
    override fun getItemViewType(position: Int): Int {
        return position
    }
    //ViewBinder 만들어질 때 실행할 동작
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val viewBinding = ItemElderInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DataViewHolder(viewBinding)
    }

    //ViewHolder가 실제로 데이터를 표시해야 할 때 호출되는 함수
    override fun onBindViewHolder(holder: DataViewHolder, position: Int)  {
        holder.bind(datalist[position], position, holder)
    }

    override fun getItemCount(): Int = datalist.size

}