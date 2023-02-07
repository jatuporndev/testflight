package com.ntbx.android.test.flight

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.ntbx.android.test.flight.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        checkPermission()
    }

    private fun checkPermission() {
        var allow = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            allow = this.packageManager.canRequestPackageInstalls()
        } else {
            try {
                allow = Settings.Secure.getInt(
                    contentResolver,
                    Settings.Secure.INSTALL_NON_MARKET_APPS
                ) == 1
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
        }
        if (!allow) {
            val snack = Snackbar.make(
                binding.root,
                "Permission is required to install the app.",
                Snackbar.LENGTH_INDEFINITE
            )
            snack.setActionTextColor(Color.WHITE)
            snack.setAction("OPEN") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                            Uri.parse("package:com.ntbx.android.test.flight")
                        )
                    )
                } else {
                    startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                }
            }
            snack.show()
        }
    }
}