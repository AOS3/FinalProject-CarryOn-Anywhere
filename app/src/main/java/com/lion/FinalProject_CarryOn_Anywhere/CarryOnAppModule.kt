package com.lion.FinalProject_CarryOn_Anywhere

import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.BannerRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.PlanRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.RequestRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.BannerService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.RequestService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CarryOnAppModule {

    @Provides
    @Singleton
    fun tripRepositoryProvider() : TripRepository{
        return TripRepository()
    }

    @Provides
    @Singleton
    fun tripServiceProvider(tripRepository: TripRepository) : TripService {
        return TripService(tripRepository)
    }

    @Provides
    @Singleton
    fun planRepositoryProvider() : PlanRepository {
        return PlanRepository()
    }

    @Provides
    @Singleton
    fun planServiceProvider(planRepository: PlanRepository) : PlanService {
        return PlanService(planRepository)
    }

    @Provides
    @Singleton
    fun requestRepositoryProvider() : RequestRepository {
        return RequestRepository()
    }

    @Provides
    @Singleton
    fun requestServiceProvider(requestRepository: RequestRepository) : RequestService {
        return RequestService(requestRepository)
    }

    @Provides
    @Singleton
    fun bannerRepositoryProvider() : BannerRepository {
        return BannerRepository()
    }

    @Provides
    @Singleton
    fun bannerServiceProvider(bannerRepository: BannerRepository) : BannerService {
        return BannerService(bannerRepository)
    }
}