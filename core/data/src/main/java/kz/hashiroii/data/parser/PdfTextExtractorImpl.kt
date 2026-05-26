package kz.hashiroii.data.parser

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.hashiroii.domain.parser.PdfTextExtractor
import javax.inject.Inject

class PdfTextExtractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PdfTextExtractor {

    override suspend fun extract(uri: Uri): String = withContext(Dispatchers.IO) {
        PDFBoxResourceLoader.init(context)

        val stream = context.contentResolver.openInputStream(uri)
            ?: error("Could not open stream for URI: $uri")

        stream.use { input ->
            PDDocument.load(input).use { document ->
                PDFTextStripper().getText(document)
            }
        }
    }
}