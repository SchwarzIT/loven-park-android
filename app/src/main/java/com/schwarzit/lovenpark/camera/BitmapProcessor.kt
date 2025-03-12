package com.schwarzit.lovenpark.camera

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.schwarzit.lovenpark.signal.*
import io.realm.kotlin.internal.platform.fileExists
import java.io.*
import java.util.*

class BitmapProcessor(val context: Context) {

    @Throws(FileNotFoundException::class)
    fun processBitmap(
        uri: Uri?,
        requiredSize: Int,
        isCameraPreview: Boolean
    ): Bitmap? {
        val filePath = uri?.let { getPath(context, it) }
        val bitmapFactoryOptions = BitmapFactory.Options()
        bitmapFactoryOptions.inJustDecodeBounds = true

        BitmapFactory.decodeStream(
            uri?.let { context.contentResolver.openInputStream(it) },
            null,
            bitmapFactoryOptions
        )

        var widthTmp = bitmapFactoryOptions.outWidth
        var heightTmp = bitmapFactoryOptions.outHeight
        var scale = 1

        while (true) {
            if (widthTmp / 2 < requiredSize || heightTmp / 2 < requiredSize) break
            widthTmp /= 2
            heightTmp /= 2
            scale *= 2
        }

        val bitmapFactoryOptions2 = BitmapFactory.Options()
        bitmapFactoryOptions2.inSampleSize = scale

        val decodedBitmap = BitmapFactory.decodeStream(
            uri?.let { context.contentResolver.openInputStream(it) },
            null,
            bitmapFactoryOptions2
        )

        val scaledBitmap = decodedBitmap?.let {
            if (isCameraPreview) {
                Bitmap.createScaledBitmap(it, DST_WITH_HEIGHT, DST_WITH_HEIGHT, true)
            } else {
                Bitmap.createScaledBitmap(
                    it,
                    (widthTmp * SCALE).toInt(),
                    (heightTmp * SCALE).toInt(),
                    true
                )
            }

        }

        return rotateBitmap(scaledBitmap, filePath)
    }

    private fun rotateBitmap(scaledBitmap: Bitmap?, filePath: String?): Bitmap? {
        var exifInterface: ExifInterface? = null

        try {
            exifInterface = filePath?.let { ExifInterface(it) }
        } catch (e: IOException) {
            Log.e(BitmapProcessor::javaClass.name, "${e.message}")
        }

        val matrix = Matrix()

        val orientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.setRotate(ORIENTATION_ROTATE_90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.setRotate(ORIENTATION_ROTATE_180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.setRotate(ORIENTATION_ROTATE_270)
            }
        }

        return scaledBitmap?.let {
            Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        }
    }

    fun localFileExist(localPathOrUri: String?, context: Context): Boolean {
        if (localPathOrUri.isNullOrEmpty()) {
            return false
        }
        val exists = File(localPathOrUri).exists()
        if (exists) {
            return exists
        }

        val contentResolver = context.contentResolver
        val uri = Uri.parse(localPathOrUri)

        try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                inputStream.close()
                return true
            }
        } catch (e: Exception) {
            Log.d(BitmapProcessor::javaClass.name, e.toString())
        }
        return exists
    }


    fun getPath(context: Context?, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if (type == TYPE) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return context?.let { getDataColumn(it, contentUri, selection, selectionArgs) }
            }
        } else if (CONTENT.equals(uri.scheme, ignoreCase = true)) {
            return context?.let { getDataColumn(it, uri, null, null) }
        } else if (FILE.equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = COLUMN
        val projection = arrayOf(column)
        try {
            cursor = uri?.let {
                context.contentResolver.query(
                    it,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
            }
            if (cursor != null && cursor.moveToFirst()) {
                val columIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isMediaDocument(uri: Uri) = MEDIA_DOCUMENTS == uri.authority

    fun getMimeType(uri: Uri) =
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr: ContentResolver = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.getDefault())
            )
        }

    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream)
        return stream.toByteArray()
    }

    fun deleteFile(uri: Uri) {
        val path = uri.path
        val fileExists = path?.let { fileExists(it) }
        if (fileExists == true && uri.toString().contains(LP_SIGNAL)) {
            val file = File(path)
            file.canonicalFile.delete()
        }
    }
}