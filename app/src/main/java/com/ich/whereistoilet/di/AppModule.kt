package com.ich.whereistoilet.di

import com.ich.whereistoilet.data.repository.ToiletInfoRepositoryImpl
import com.ich.whereistoilet.domain.repository.ToiletInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideToiletInfoRepository(): ToiletInfoRepository{
        return ToiletInfoRepositoryImpl()
    }
}