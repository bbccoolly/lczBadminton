package com.lcz.bm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lcz.bm.databinding.ItemAdapterTxtBinding
import com.lcz.bm.entity.ShowMsgEntity
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-11
 */
class RecyclerMsgAdapter @Inject constructor() :  ListAdapter<ShowMsgEntity, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RViewHolder(
            ItemAdapterTxtBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as RViewHolder).bind(plant)
    }

    class RViewHolder(private val binding: ItemAdapterTxtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowMsgEntity) {
            binding.apply {
                entity = item
                executePendingBindings()
            }
        }
    }

}

private class PlantDiffCallback : DiffUtil.ItemCallback<ShowMsgEntity>() {

    override fun areItemsTheSame(oldItem: ShowMsgEntity, newItem: ShowMsgEntity): Boolean {
        return oldItem.showMsg == newItem.showMsg
    }

    override fun areContentsTheSame(oldItem: ShowMsgEntity, newItem: ShowMsgEntity): Boolean {
        return oldItem == newItem
    }
}
