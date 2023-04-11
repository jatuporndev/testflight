package com.ntbx.android.test.flight.fragment.appList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.ntbx.android.test.flight.fragment.models.AppList
import com.ntbx.android.test.flight.util.Resource
import com.ntbx.android.test.flight.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.math.RoundingMode

class AppListViewModel : ViewModel() {

    private val _appList = MutableLiveData<Resource<List<AppList>>>()
    val appList: LiveData<Resource<List<AppList>>>
        get() = _appList

    fun getApp(env: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _appList.postValue(Resource.Loading())
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child(env)
            val result: ArrayList<AppList> = ArrayList()
            val listRef = storageRef.listAll()
            var counter = 0

            listRef.addOnSuccessListener { listResult ->
                if(listResult.items.isNotEmpty()){
                    val totalItems = listResult.items.size
                    listResult.items.forEach { item ->
                        item.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            item.metadata.addOnSuccessListener { metadata ->
                                val sizeBytes = metadata.sizeBytes
                                result.add(
                                    AppList(
                                        appName = item.name,
                                        appUrl = downloadUrl,
                                        sizeBytes = sizeBytes,
                                        createDate = Util.getFormatTime(metadata.creationTimeMillis)
                                    )
                                )
                                counter++
                                if(counter == totalItems){
                                    _appList.postValue(Resource.Success(result))
                                }
                            }
                        }
                    }
                }else{
                    _appList.postValue(Resource.Success(result))
                }
            }.addOnFailureListener {
                if (it is StorageException && it.errorCode == StorageException.ERROR_QUOTA_EXCEEDED) {
                    Log.d("Size",it.stackTrace.toString())
                }
                _appList.postValue(Resource.Error("Something wrong"))
            }
        }

    }
}