package com.ntbx.android.test.flight.fragment.appList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.databinding.CardAppListBinding
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.fragment.models.DownloadState
import com.ntbx.android.test.flight.util.Util


class AppListAdapter : ListAdapter<AppList, AppListAdapter.ViewHolder>(MyDiffItemCallback()) {
    private val appListFragment = AppListFragment.appListFragment

    class ViewHolder(val binding: CardAppListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardAppListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            viewFlipper.displayedChild = 0
            appName.text = data.appName
            btnDowload.text = if (data.isUpdate) "Update" else "Install"
            val sizeMb = Util.getSizeMb(data.sizeBytes)
            btnDowload.setOnClickListener {
                appListFragment.downloadFile(
                    url = data.appUrl,
                    appName = data.appName
                ) { progress ->
                    updateUI(holder.binding, DownloadState.DOWNLOADING)
                    dowloadStatus.text = "${progress}% of $sizeMb MB"
                    dowloadStatus.visibility = View.VISIBLE
                    if (progress > 99) {
                        btnDowload.text = "Update"
                        updateUI(holder.binding, DownloadState.INSTALL)
                    }
                }
            }

            btnCancel.setOnClickListener {
                if (data.downLoadId >= 0) {
                    appListFragment.cancelDownload(data.downLoadId)
                    updateUI(holder.binding, DownloadState.INSTALL)
                }

            }
        }
    }


    private fun updateUI(binding: CardAppListBinding, state: DownloadState) {
        when (state) {
            DownloadState.INSTALL -> {
                binding.viewFlipper.displayedChild = 0
                binding.loading.visibility = View.GONE
                binding.dowloadStatus.visibility = View.GONE
            }
            DownloadState.DOWNLOADING -> {
                binding.viewFlipper.displayedChild = 1
                binding.loading.visibility = View.VISIBLE
            }
            DownloadState.UPDATE -> {}
        }
    }


    internal class MyDiffItemCallback : DiffUtil.ItemCallback<AppList>() {
        override fun areItemsTheSame(oldItem: AppList, newItem: AppList): Boolean {
            return oldItem.appName == newItem.appName
        }

        override fun areContentsTheSame(oldItem: AppList, newItem: AppList): Boolean {
            return oldItem == newItem
        }
    }
}