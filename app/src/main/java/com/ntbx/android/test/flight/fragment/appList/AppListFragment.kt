package com.ntbx.android.test.flight.fragment.appList

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.ntbx.android.test.flight.customView.HandleUpdateSheet
import com.ntbx.android.test.flight.databinding.FragmentAppListBinding
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.util.Resource
import com.ntbx.android.test.flight.util.Util.formatKey
import kotlinx.coroutines.*
import java.io.File


class AppListFragment : Fragment(), IAppListFragment {
    companion object {
        @JvmStatic
        lateinit var appListFragment: IAppListFragment
    }

    init {
        appListFragment = this
    }

    private val args: AppListFragmentArgs by navArgs()
    private lateinit var binding: FragmentAppListBinding
    private lateinit var appListAdapter: AppListAdapter
    lateinit var bottomSheet : HandleUpdateSheet
    private val appViewModel: AppListViewModel by viewModels()
    lateinit var downloadManager: DownloadManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        appListAdapter = AppListAdapter(requireContext())
        binding.txtTitle.text = args.key
        binding.recycleview.adapter = appListAdapter
        binding.recycleview.layoutManager = GridLayoutManager(requireContext(), 1)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        appViewModel.getApp(formatKey(args.key))
        observer()
    }

    private fun observer() {
        appViewModel.appList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.viewFlipper.displayedChild = 0
                is Resource.Success -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.viewFlipper.displayedChild = 1
                        appListAdapter.submitList(it.data)
                    } else {
                        binding.viewFlipper.displayedChild = 2
                    }
                }
                is Resource.Error -> {
                    binding.viewFlipper.displayedChild = 2
                }
            }
        }
    }

    @SuppressLint("Range")
    override fun downloadFile(url: String, appName: String, onProgress: ((Int) -> Unit)?) {
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setTitle(appName)
        request.setDescription("Please wait...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalFilesDir(context, null, appName)
        val apkFile = File(requireContext().getExternalFilesDir(null), appName)
        if (apkFile.exists()) {
            if (apkFile.delete()) {
                println("old file deleted")
            }
        }
            onProgress?.invoke(0)
            CoroutineScope(Dispatchers.IO).launch {
                val downloadId = downloadManager.enqueue(request)
                val updateObserve = Observer<Resource<List<AppList>>> { data ->
                    data.data?.find { it.appName == appName }?.downLoadId = downloadId
                }
                withContext(Dispatchers.Main) {
                    appViewModel.appList.observe(viewLifecycleOwner, updateObserve)
                    appViewModel.appList.removeObserver(updateObserve)
                }

                var lastDownloadProgress = -1f
                while (true) {
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        val downloadProgress = ((bytesDownloaded * 100f) / bytesTotal)
                        if (downloadProgress != lastDownloadProgress) {
                            lastDownloadProgress = downloadProgress
                            withContext(Dispatchers.Main) {
                                onProgress?.invoke(downloadProgress.toInt())
                            }
                        }
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            withContext(Dispatchers.Main) {
                                onProgress?.invoke(100)
                            }
                            break
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            Toast.makeText(requireContext(), "fail, try again!", Toast.LENGTH_SHORT).show()
                            break
                        }
                        delay(1000)
                    }
                    cursor.close()
                }
                withContext(Dispatchers.Main) { installApp(apkFile) }
            }

    }

    override fun cancelDownload(downloadId: Long) {
        downloadManager.remove(downloadId)
    }

    override fun installApp(apkFile: File) {
        try {
            val apkUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                apkFile
            )
            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            intent.data = apkUri
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireActivity().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Installing Failure", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    override fun cancelAll(){
        val query = DownloadManager.Query()
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING)
        val cursor = downloadManager.query(query)
        val downloadIds = ArrayList<Long>()
        if (cursor.moveToFirst()) {
            do {
                downloadIds.add(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        for (downloadId in downloadIds) {
            downloadManager.remove(downloadId)
        }
    }

}