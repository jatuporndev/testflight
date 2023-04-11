package com.ntbx.android.test.flight.fragment.appList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ntbx.android.test.flight.R
import com.ntbx.android.test.flight.databinding.CardAppListBinding
import com.ntbx.android.test.flight.databinding.CardAppListV2Binding
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.fragment.models.DownloadState
import com.ntbx.android.test.flight.util.Util
import java.io.File


class AppListAdapter(val context: Context) : ListAdapter<AppList, AppListAdapter.ViewHolder>(MyDiffItemCallback()) {
    private val appListFragment = AppListFragment.appListFragment

    class ViewHolder(val binding: CardAppListV2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardAppListV2Binding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        val apkFile = File(context.getExternalFilesDir(null), data.appName)
        holder.binding.apply {
            viewFlipper.displayedChild = 0
            appName.text = data.appName
            txtDate.text = data.createDate
            if(data.appName.endsWith(".apk")) {
                if(apkFile.exists()){
                    btnDownload.text = "Update"
                    iconAndroid.setImageResource(R.drawable.ic_baseline_android_green)
                    dowloadStatus.visibility = View.VISIBLE
                    btnInstall.visibility = View.VISIBLE
                    dowloadStatus.text = "Downloaded"
                }else{
                    btnDownload.text = "GET"
                    iconAndroid.setImageResource(R.drawable.ic_baseline_android_24)
                }
            }else {
                iconAndroid.setImageResource(R.drawable.ic_baseline_block_24)
                btnDownload.isEnabled = false
                btnInstall.isEnabled = false
            }

            val sizeMb = Util.getSizeMb(data.sizeBytes)
            btnDownload.setOnClickListener {
                appListFragment.downloadFile(
                    url = data.appUrl,
                    appName = data.appName
                ) { progress ->
                    updateUI(holder.binding, DownloadState.DOWNLOADING)
                    dowloadStatus.text = "${progress}% of $sizeMb MB"
                    dowloadStatus.visibility = View.VISIBLE
                    if (progress > 99) {
                        updateUI(holder.binding, DownloadState.INSTALL)
                        notifyItemChanged(position)
                    }
                }
            }

            btnInstall.setOnClickListener {
                appListFragment.installApp(apkFile)
            }

            btnCancel.setOnClickListener {
                if (data.downLoadId >= 0) {
                    appListFragment.cancelDownload(data.downLoadId)
                    updateUI(holder.binding, DownloadState.INSTALL)
                    notifyItemChanged(position)
                }
            }
        }
    }


    private fun updateUI(binding: CardAppListV2Binding, state: DownloadState) {
        when (state) {
            DownloadState.INSTALL -> {
                binding.viewFlipper.displayedChild = 0
                binding.loading.visibility = View.GONE
                binding.dowloadStatus.visibility = View.GONE
            }
            DownloadState.DOWNLOADING -> {
                binding.viewFlipper.displayedChild = 1
                binding.loading.visibility = View.VISIBLE
                binding.btnInstall.visibility = View.GONE
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