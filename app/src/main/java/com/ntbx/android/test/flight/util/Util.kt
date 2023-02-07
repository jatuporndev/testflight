package com.ntbx.android.test.flight.util

import java.math.RoundingMode

object Util {

    fun getSizeMb(sizeBytes: Long): String {
        return (sizeBytes.toDouble() / (1024 * 1024)).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
    }
}