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

    fun setTitle(appName: String) : HandleUpdateSheet {
        binding.txtTitle.text = "$appName is already installed."
        return this
    }


    fun onUnInstallButtonClick(
        @Nullable listener: ClickListener
    ): HandleUpdateSheet {
        binding.btnUnInstall.setOnClickListener {
            listener.click()
            this.dismiss()
        }
        return this
    }
}