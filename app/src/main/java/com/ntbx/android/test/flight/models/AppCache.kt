package com.ntbx.android.test.flight.models

import java.io.File

data class AppCache(
    var appName: String = "",
    var sizeBytes: Long = 0L,
    var appFile: File? = null

)
