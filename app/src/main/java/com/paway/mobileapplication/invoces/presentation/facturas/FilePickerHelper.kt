package com.paway.mobileapplication.invoces.presentation.facturas

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class FilePickerHelper(private val context: Context) {
    fun getFileFromUri(uri: Uri): ByteArray? {
        return context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    }
}

@Composable
fun rememberFilePicker(onFilePicked: (ByteArray?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val filePickerHelper = remember { FilePickerHelper(context) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileBytes = filePickerHelper.getFileFromUri(it)
            onFilePicked(fileBytes)
        } ?: onFilePicked(null)
    }

    return { launcher.launch("*/*") }
}
