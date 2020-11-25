package com.lcz.bm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lcz.bm.databinding.ItemAdapterTxtBinding

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-06
 */
class ShowMsgAdapter(
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<String, ShowMsgViewHolder>(ShowMsgDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowMsgViewHolder {
        val binding =
            ItemAdapterTxtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowMsgViewHolder(binding, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: ShowMsgViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}

class ShowMsgViewHolder(
    private val binding: ItemAdapterTxtBinding,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(entity: String) {
        binding.showMsg = entity
        binding.lifecycleOwner = lifecycleOwner
    }
}

object ShowMsgDiff : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}