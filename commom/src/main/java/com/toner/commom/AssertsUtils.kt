package com.toner.commom

import android.content.Context
import java.io.File

fun copyAssertsToData(context: Context, assertFileName: String, dataPath: String): String {
    val inputStream = context.assets.open(assertFileName)
    val output = File(dataPath)
    val outputStream = output.outputStream()
    val readBuffer = ByteArray(1024)
    while (inputStream.read(readBuffer) != -1) {
        outputStream.write(readBuffer)
    }
    return output.absolutePath
}