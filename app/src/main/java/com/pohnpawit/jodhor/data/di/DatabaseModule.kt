package com.pohnpawit.jodhor.data.di

import android.content.Context
import androidx.room.Room
import com.pohnpawit.jodhor.data.local.db.DormDao
import com.pohnpawit.jodhor.data.local.db.JodHorDatabase
import com.pohnpawit.jodhor.data.local.db.PhotoDao
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
        Room.databaseBuilder(context, JodHorDatabase::class.java, "jodhor.db").build()

    @Provides
    fun provideDormDao(db: JodHorDatabase): DormDao = db.dormDao()

    @Provides
    fun providePhotoDao(db: JodHorDatabase): PhotoDao = db.photoDao()
}
