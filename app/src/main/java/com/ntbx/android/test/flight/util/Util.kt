package com.ntbx.android.test.flight.util

import com.ntbx.android.test.flight.R
import java.math.RoundingMode

object Util {

    fun getSizeMb(sizeBytes: Long): String {
        return (sizeBytes.toDouble() / (1024 * 1024)).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
    }

    fun getIconApp(appName: String): Int {
        return when (appName) {
            "consumer" -> R.drawable.ic_consumer_icon
            "internal" -> R.drawable.ic_internal
            else -> R.drawable.ic_baseline_more_horiz_24
        }
    }
}