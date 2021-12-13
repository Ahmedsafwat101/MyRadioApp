package com.myradio.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.myradio.utils.Constants.OUTPUT_PATH
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileBuilderManager {
    @Throws(FileNotFoundException::class)
    fun writeBitmapToFile(applicationContext: Context, byteArray:ByteArray): Uri {

        val name = String.format("radio-%s.wav", getCurrentDateTime())
        val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            out.write(byteArray)

        } finally {
            out?.let {
                try {
                    it.close()
                } catch (ignore: IOException) {
                }

            }
        }
        return Uri.fromFile(outputFile)
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}