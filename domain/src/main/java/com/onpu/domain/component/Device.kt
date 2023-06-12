package com.onpu.domain.component

interface Device {
    val androidId: String
    val userCountry: String
    val locale: String
    val deviceManufacturerAndModel: String
    val sdk: String
    val screenSize: String
    val appVersion: String
}