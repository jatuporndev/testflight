package com.ntbx.android.test.flight.fragment.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.databinding.CardEnvBinding
import com.ntbx.android.test.flight.util.Util

class HomeAdapter : ListAdapter<String,HomeAdapter.ViewHolder>(MyDiffItemCallback()) {
    class ViewHolder(val binding: CardEnvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(CardEnvBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data = getItem(position)
        holder.binding.apply {
            envName.text = data
            iconApp.setImageResource(Util.getIconApp(data))
            iconApp.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            if(data != "other"){
                it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEnvListFragment(data))
            }else {
                it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAppListFragment(data))
            }

        }
    }


    internal class MyDiffItemCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}