package com.ntbx.android.test.flight.fragment.models

data class AppList(
    var appName :String,
    var appUrl: String,
    var isUpdate: Boolean,
    var downLoadId: Long = -1,
    var sizeBytes: Long
)
