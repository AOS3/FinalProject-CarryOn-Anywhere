package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.di_social

import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.CarryTalkRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripReviewRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.CarryTalkService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripReviewModule {

    @Provides
    @Singleton
    fun provideTripReviewRepository(): TripReviewRepository {
        return TripReviewRepository()
    }

    @Provides
    @Singleton
    fun provideTripReviewService(): TripReviewService {
        return TripReviewService()
    }

    @Provides
    @Singleton
    fun provideCarryTalkRepository(): CarryTalkRepository {
        return CarryTalkRepository()
    }

    @Provides
    @Singleton
    fun provideCarryTalkService(): CarryTalkService {
        return CarryTalkService()
    }
}