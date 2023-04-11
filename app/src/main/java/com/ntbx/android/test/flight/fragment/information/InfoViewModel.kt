package com.ntbx.android.test.flight.fragment.information

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.util.Resource
import com.ntbx.android.test.flight.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class InfoViewModel : ViewModel() {

    private val _appList = MutableLiveData<Resource<List<AppList>>>()
    val appList: LiveData<Resource<List<AppList>>>
        get() = _appList

    fun getSizeCache(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _appList.postValue(Resource.Loading())
                var t = 0L
                val tempAppList : ArrayList<AppList> = ArrayList()
                val externalStorageDirectory = context.getExternalFilesDir(null).toString()
                val rootDirectory = File(externalStorageDirectory)
                val files = rootDirectory.listFiles()
                for (file in files!!) {
                    t += file.length()
                    tempAppList.add(AppList(appName = file.name, sizeBytes = file.length()))
                }
                _appList.postValue(Resource.Success(tempAppList))
            }catch (_:Exception) {
                _appList.postValue(Resource.Error("exception"))
            }
        }
    }

}