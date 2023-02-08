package com.ntbx.android.test.flight.fragment.envList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.databinding.CardEnvBinding
import com.ntbx.android.test.flight.models.Key

class EnvListAdapter : ListAdapter<Key, EnvListAdapter.ViewHolder>(MyDiffItemCallback()) {
    class ViewHolder(val binding: CardEnvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardEnvBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            envName.text = data.env
        }

        holder.itemView.setOnClickListener {
            val key = "${data.appName} / ${data.env}"
            it.findNavController()
                .navigate(EnvListFragmentDirections.actionEnvListFragmentToAppListFragment(key))
        }
    }

    internal class MyDiffItemCallback : DiffUtil.ItemCallback<Key>() {
        override fun areItemsTheSame(oldItem: Key, newItem: Key): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Key, newItem: Key): Boolean {
            return oldItem == newItem
        }
    }
}