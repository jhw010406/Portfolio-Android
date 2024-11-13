package com.example.tradingapp.model.data.request

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class RequestBodyFromContentURI (
    context: Context,
    private val uri: Uri
) : RequestBody() {

    private val contentResolver = context.contentResolver

    var filename = ""
    var contentType : String? = null
    var size = -1L

    init {
        contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                size = cursor.getLong(0)
                filename = cursor.getString(1)
            }
        }

        contentType = contentResolver.getType(uri)
    }

    fun getUri() : Uri {
        return uri
    }

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        contentResolver.getType(uri)?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        contentResolver.openInputStream(uri)?.source()?.use { source ->
            sink.writeAll(source)
        }
    }

}