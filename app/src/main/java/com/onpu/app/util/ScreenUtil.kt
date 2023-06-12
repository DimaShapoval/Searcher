package com.onpu.app.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

object ScreenUtil {
    fun getScreenDensity(context: Context) : String {
        val resources: Resources = context.resources
        val displayMetrics: DisplayMetrics = resources.displayMetrics

        println("dwakdkawh " + displayMetrics.densityDpi)
        val densityType: ScreenDensity = when (displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> ScreenDensity.LDPI
            DisplayMetrics.DENSITY_MEDIUM -> ScreenDensity.MDPI
            DisplayMetrics.DENSITY_HIGH -> ScreenDensity.HDPI
            DisplayMetrics.DENSITY_XHIGH -> ScreenDensity.XHDPI
            DisplayMetrics.DENSITY_XXHIGH -> ScreenDensity.XXHDPI
            DisplayMetrics.DENSITY_XXXHIGH -> ScreenDensity.XXXHDPI
            else -> ScreenDensity.UNKNOWN
        }

        return displayMetrics.densityDpi.toString()
    }
}

enum class ScreenDensity{
    LDPI, MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI, UNKNOWN
}