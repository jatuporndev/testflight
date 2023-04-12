package com.ntbx.android.test.flight.fragment.information

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.MainActivity
import com.ntbx.android.test.flight.databinding.CardAppTempBinding
import com.ntbx.android.test.flight.models.AppCache
import com.ntbx.android.test.flight.util.Util

class CacheAdapter : ListAdapter<AppCache, CacheAdapter.ViewHolder>(MyDiffItemCallback()) {
    class ViewHolder(val binding: CardAppTempBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(CardAppTempBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            txtTempName.text = data.appName
            txtTempSize.text = "${Util.getSizeMb(data.sizeBytes)} MB"
        }

        holder.binding.txtBtnInstall.setOnClickListener {
            data.appFile?.let { file -> MainActivity.mainActivity.installApp(file) }
        }
    }

    internal class MyDiffItemCallback : DiffUtil.ItemCallback<AppCache>() {
        override fun areItemsTheSame(oldItem: AppCache, newItem: AppCache): Boolean {
            return oldItem.appName == newItem.appName
        }

        override fun areContentsTheSame(oldItem: AppCache, newItem: AppCache): Boolean {
            return oldItem == newItem
        }
    }
}