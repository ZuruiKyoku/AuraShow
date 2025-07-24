package com.slygames.aurashow

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract

fun getImagesFromTreeUri(contentResolver: ContentResolver, treeUri: Uri): List<Uri> {
    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri,
        DocumentsContract.getTreeDocumentId(treeUri))
    val imageUris = mutableListOf<Uri>()

    val cursor: Cursor? = contentResolver.query(childrenUri, arrayOf(
        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
        DocumentsContract.Document.COLUMN_MIME_TYPE,
    ), null, null, null)

    cursor?.use {
        while (it.moveToNext()) {
            val documentId = it.getString(0)
            val mimeType = it.getString(1)
            if (mimeType.startsWith("image/")) {
                val documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
                imageUris.add(documentUri)
            }
        }
    }
    return imageUris
}