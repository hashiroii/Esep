package kz.hashiroii.domain.parser

import android.net.Uri

interface PdfTextExtractor {
    suspend fun extract(uri: Uri): String
}