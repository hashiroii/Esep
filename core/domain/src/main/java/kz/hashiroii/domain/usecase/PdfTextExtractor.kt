package kz.hashiroii.domain.usecase

import android.net.Uri

interface PdfTextExtractor {
    suspend fun extract(uri: Uri): String
}