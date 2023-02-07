package com.ntbx.android.test.flight.fragment.information

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ntbx.android.test.flight.R
import com.ntbx.android.test.flight.databinding.FragmentInfoBinding
import com.ntbx.android.test.flight.util.Util
import java.io.File

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSizeCache()

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnClear.setOnClickListener {
            clearCache()
        }

    }


    @SuppressLint("SetTextI18n")
    private fun getSizeCache() {
        var t = 0L
        val externalStorageDirectory = requireContext().getExternalFilesDir(null).toString()
        val rootDirectory = File(externalStorageDirectory)
        val files = rootDirectory.listFiles()
        for (file in files!!) {
            t += file.length()
        }
        binding.txtSize.text = "${Util.getSizeMb(t)} MB"
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