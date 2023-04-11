package com.ntbx.android.test.flight.fragment.information

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ntbx.android.test.flight.BuildConfig
import com.ntbx.android.test.flight.databinding.FragmentInfoBinding
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.util.Resource
import com.ntbx.android.test.flight.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    lateinit var cacheAdapter: CacheAdapter
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoViewModel.getSizeCache(requireContext())
        cacheAdapter = CacheAdapter()
        binding.btnClear.isEnabled = false
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = cacheAdapter

        binding.txtAppVersion.text = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
        BuildConfig.APPLICATION_ID
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnClear.setOnClickListener {
            clearCache()
        }

        getSizeCache()
    }

    @SuppressLint("SetTextI18n")
    private fun getSizeCache() {
        infoViewModel.appList.observe(viewLifecycleOwner) {
            if(!it.data.isNullOrEmpty()) {
                when (it) {
                    is Resource.Loading -> {
                        binding.loadingInfo.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        var allSize = 0L
                        for (item in it.data) {
                            allSize += item.sizeBytes
                        }
                        binding.loadingInfo.visibility = View.GONE
                        binding.txtSize.text = "${Util.getSizeMb(allSize)} MB"
                        cacheAdapter.submitList(it.data)
                        binding.btnClear.isEnabled = true

                    }
                    is Resource.Error -> {
                        binding.loadingInfo.visibility = View.GONE
                    }
                }
            }

        }

        CoroutineScope(Dispatchers.IO).launch {
            var t = 0L
            val tempAppList: ArrayList<AppList> = ArrayList()
            val externalStorageDirectory = requireContext().getExternalFilesDir(null).toString()
            val rootDirectory = File(externalStorageDirectory)
            val files = rootDirectory.listFiles()
            for (file in files!!) {
                t += file.length()
                tempAppList.add(AppList(appName = file.name, sizeBytes = file.length()))
            }
            withContext(Dispatchers.Main) {
                binding.txtSize.text = "${Util.getSizeMb(t)} MB"
                cacheAdapter.submitList(tempAppList)
            }
        }
    }

    private fun clearCache() {
        binding.btnClear.isEnabled = false
        val externalStorageDirectory = requireContext().getExternalFilesDir(null).toString()
        val rootDirectory = File(externalStorageDirectory)
        val files = rootDirectory.listFiles()
        for (file in files!!) {
            file.delete()
        }
        binding.btnClear.isEnabled = true
        getSizeCache()
    }

}