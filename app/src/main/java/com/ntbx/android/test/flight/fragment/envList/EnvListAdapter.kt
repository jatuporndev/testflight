package com.ntbx.android.test.flight.fragment.envList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.databinding.CardEnvBinding
import com.ntbx.android.test.flight.fragment.home.HomeAdapter
import com.ntbx.android.test.flight.util.Util.getIconApp

class EnvListAdapter : ListAdapter<String, HomeAdapter.ViewHolder>(HomeAdapter.MyDiffItemCallback()) {
    class ViewHolder(val binding: CardEnvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        return HomeAdapter.ViewHolder(CardEnvBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            envName.text = data
        }

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(EnvListFragmentDirections.actionEnvListFragmentToAppListFragment(data))
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