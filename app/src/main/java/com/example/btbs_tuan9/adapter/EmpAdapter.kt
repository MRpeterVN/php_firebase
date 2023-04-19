package com.example.btbs_tuan9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.btbs_tuan9.EmployeeModel
import com.example.btbs_tuan9.databinding.EmpListItemBinding

class EmpAdapter(private var ds: ArrayList<EmployeeModel>) :
    RecyclerView.Adapter<EmpAdapter.ViewHolder>() {

    //__________________________click item________________________________
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    //____________________________________________________________________


    class ViewHolder(val binding: EmpListItemBinding, clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = EmpListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //        return ViewHolder(itemView)
        val itemView = EmpListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTenSP.text = ds[position].tenSp
    }

    override fun getItemCount(): Int {
        return ds.size
    }
}


