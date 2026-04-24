package com.pohnpawit.jodhor.data.di

import android.content.Context
import androidx.room.Room
import com.pohnpawit.jodhor.data.local.db.DormDao
import com.pohnpawit.jodhor.data.local.db.JodHorDatabase
import com.pohnpawit.jodhor.data.local.db.PhoneContactDao
import com.pohnpawit.jodhor.data.local.db.PhotoDao
import com.pohnpawit.jodhor.data.local.db.migration.MIGRATION_1_2
import com.pohnpawit.jodhor.data.local.db.migration.MIGRATION_2_3
import com.pohnpawit.jodhor.data.local.db.migration.MIGRATION_3_4
import com.pohnpawit.jodhor.data.local.db.migration.MIGRATION_4_5
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JodHorDatabase =
        Room.databaseBuilder(context, JodHorDatabase::class.java, "jodhor.db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .build()

    @Provides
    fun provideDormDao(db: JodHorDatabase): DormDao = db.dormDao()

    @Provides
    fun providePhotoDao(db: JodHorDatabase): PhotoDao = db.photoDao()

    @Provides
    fun providePhoneContactDao(db: JodHorDatabase): PhoneContactDao = db.phoneContactDao()
}
