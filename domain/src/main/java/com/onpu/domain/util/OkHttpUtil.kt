package com.onpu.domain.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


fun File.toMultipartBody(name: String = "file", mediaType: String = "multipart/form-data") = MultipartBody
    .Part
    .createFormData(name, this.name, asRequestBody(mediaType.toMediaType()))