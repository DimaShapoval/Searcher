package com.onpu.domain.util

import java.io.File

public fun File.readFile(): String = this.bufferedReader().readText()