package com.pohnpawit.jodhor.data.di

import com.pohnpawit.jodhor.data.repository.DormRepository
import com.pohnpawit.jodhor.data.repository.DormRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDormRepository(impl: DormRepositoryImpl): DormRepository
}
