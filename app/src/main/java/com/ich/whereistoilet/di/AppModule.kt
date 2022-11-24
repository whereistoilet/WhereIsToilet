package com.ich.whereistoilet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideDatabaseReference(): DatabaseReference = Firebase.database.reference
}