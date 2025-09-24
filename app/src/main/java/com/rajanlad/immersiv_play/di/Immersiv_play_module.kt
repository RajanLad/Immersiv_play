package com.rajanlad.immersiv_play.di

import com.rajanlad.immersiv_play._usecases.FetchVideosUsecase
import com.rajanlad.immersiv_play.data.network.apis.VideoAPI
import com.rajanlad.immersiv_play.data.network.services.VideoService
import com.rajanlad.immersiv_play.data.repositories.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Immersiv_play_module{
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hls-video-samples.r2.immersiv.cloud/videos.json/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoApi(retrofit: Retrofit): VideoAPI {
        return retrofit.create(VideoAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoService(videoApi: VideoAPI): VideoService {
        return VideoService(videoApi)
    }


    @Provides
    @Singleton
    fun provideVideoRepository(videoService: VideoService): VideoRepository {
        return VideoRepository(videoService)
    }

    @Provides
    @Singleton
    fun provideFetchVideosUsecase(videoRepository: VideoRepository): FetchVideosUsecase {
        return FetchVideosUsecase(videoRepository)
    }

}