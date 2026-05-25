package kz.hashiroii.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.hashiroii.data.local.EsepDatabase
import kz.hashiroii.data.local.dto.TransactionDao
import kz.hashiroii.data.parser.KaspiStatementParser
import kz.hashiroii.data.parser.PdfTextExtractorImpl
import kz.hashiroii.data.repository.TransactionRepositoryImpl
import kz.hashiroii.domain.repository.TransactionRepository
import kz.hashiroii.domain.usecase.PdfParser
import kz.hashiroii.domain.usecase.PdfTextExtractor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    // @Binds tells Hilt: "when someone asks for PdfParser, give them KaspiStatementParser"
    @Binds
    abstract fun bindPdfParser(impl: KaspiStatementParser): PdfParser

    // Same pattern: PdfTextExtractor interface → PdfTextExtractorImpl
    @Binds
    abstract fun bindPdfTextExtractor(impl: PdfTextExtractorImpl): PdfTextExtractor

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): EsepDatabase =
            Room.databaseBuilder(
                context,
                EsepDatabase::class.java,
                "esep.db"
            )
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        fun provideTransactionDao(db: EsepDatabase): TransactionDao = db.transactionDao()
    }
}