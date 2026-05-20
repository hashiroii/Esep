package kz.hashiroii.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.hashiroii.data.local.EsepDatabase
import kz.hashiroii.data.local.dto.TransactionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): EsepDatabase =
            Room.databaseBuilder(
                context,
                EsepDatabase::class.java,
                "esep.db"
            ).build()

        @Provides
        fun provideTransactionDao(db: EsepDatabase): TransactionDao = db.transactionDao()
    }
}