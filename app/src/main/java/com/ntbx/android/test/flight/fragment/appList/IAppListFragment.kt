package com.ntbx.android.test.flight.fragment.appList

interface IAppListFragment {
    fun downloadFile(url: String, appName: String, onProgress: ((Int) -> Unit)? = null)
    fun cancelDownload(downloadId: Long)
    fun cancelAll()
}