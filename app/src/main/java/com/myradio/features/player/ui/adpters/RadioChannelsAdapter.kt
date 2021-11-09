package com.myradio.features.player.ui.adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myradio.databinding.RecyclerRadioItemBinding
import com.myradio.features.player.data.models.Radio

class RadioChannelsAdapter(val playRadioChannel: (Int) -> Unit) : PagingDataAdapter<Radio,
        RadioChannelsAdapter.MyViewHolder>(diffCallback) {

    inner class MyViewHolder(val binding: RecyclerRadioItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Radio>() {
            override fun areItemsTheSame(oldItem: Radio, newItem: Radio): Boolean {
                return oldItem.channel_id == newItem.channel_id
            }

            override fun areContentsTheSame(oldItem: Radio, newItem: Radio): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            radioNameTxt.text = currentItem?.name

            val imageLink = currentItem?.image_url
            urlImg.load(imageLink) {
                crossfade(true)
                crossfade(100)
            }
        }

        holder.itemView.setOnClickListener{
            playRadioChannel(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecyclerRadioItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}