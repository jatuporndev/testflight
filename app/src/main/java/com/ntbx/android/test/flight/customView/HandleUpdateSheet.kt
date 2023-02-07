package com.ntbx.android.test.flight.customView

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ntbx.android.test.flight.databinding.HandelUpdateSheetBinding

class HandleUpdateSheet(context: Context) : BottomSheetDialogFragment() {
    interface ClickListener {
        fun click()
    }
     private var binding = HandelUpdateSheetBinding.inflate(LayoutInflater.from(context))
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    fun onInstallButtonClick(
        @Nullable listener: ClickListener
    ): HandleUpdateSheet {
        binding.btnInstall.setOnClickListener {
            listener.click()
        }
        return this
    }

    fun onUpdateButtonClick(
        @Nullable listener: ClickListener
    ): HandleUpdateSheet {
        binding.btnUpdate.setOnClickListener {
            listener.click()
        }
        return this
    }
}