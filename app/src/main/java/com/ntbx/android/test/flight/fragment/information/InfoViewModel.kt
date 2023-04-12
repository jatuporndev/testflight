package com.ntbx.android.test.flight.fragment.information

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntbx.android.test.flight.models.AppCache
import com.ntbx.android.test.flight.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class InfoViewModel : ViewModel() {

    private val _appCache= MutableLiveData<Resource<List<AppCache>>>()
    val appCache: LiveData<Resource<List<AppCache>>>
        get() = _appCache

    fun getSizeCache(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _appCache.postValue(Resource.Loading())
                var t = 0L
                val tempAppList: ArrayList<AppCache> = ArrayList()
                val externalStorageDirectory = context.getExternalFilesDir(null).toString()
                val rootDirectory = File(externalStorageDirectory)
                val files = rootDirectory.listFiles()
                for (file in files!!) {
                    t += file.length()
                    tempAppList.add(AppCache(appName = file.name, sizeBytes = file.length(), appFile = file))
                }
                if (tempAppList.isNotEmpty()) {
                    _appCache.postValue(Resource.Success(tempAppList))
                } else {
                    _appCache.postValue(Resource.Error("empty"))
                }

            } catch (_: Exception) {
                _appCache.postValue(Resource.Error("exception"))
            }
        }
    }

}