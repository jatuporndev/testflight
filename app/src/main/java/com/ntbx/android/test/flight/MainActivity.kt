package com.ntbx.android.test.flight

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.ntbx.android.test.flight.customView.HandleUpdateSheet
import com.ntbx.android.test.flight.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity(), IMainActivity {

    companion object {
        @JvmStatic
        lateinit var mainActivity: IMainActivity
    }

    init {
        mainActivity = this
    }

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

    override fun installApp(apkFile: File) {
        try {
            val packageName = getPackageNameFromApk(apkFile)
            if (packageName != null) {
                if (this.isPackageInstalled(packageName)) {
                    uninstallApp(packageName)
                } else {
                    val apkUri = FileProvider.getUriForFile(
                        this,
                        "${this.packageName}.provider",
                        apkFile
                    )
                    val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    intent.data = apkUri
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Installing Failure", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPackageNameFromApk(apkFile: File): String? {
        val packageManager = applicationContext.packageManager
        val packageInfo: PackageInfo? = packageManager.getPackageArchiveInfo(apkFile.path, 0)
        return packageInfo?.packageName
    }

    private fun uninstallApp(packageName: String) {
        val bottomSheetDialogFragment = HandleUpdateSheet(this)
        bottomSheetDialogFragment
            .setTitle(getAppNameFromPackage(packageName))
            .onUnInstallButtonClick(object : HandleUpdateSheet.ClickListener {
                override fun click() {
                    val uninstallUri = Uri.parse("package:$packageName")
                    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, uninstallUri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            })
            .show(supportFragmentManager, "bottomSheetDialogFragment")

    }

    private fun Context.isPackageInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun getAppNameFromPackage(packageName: String): String {
        val packageManager: PackageManager = this.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }
}