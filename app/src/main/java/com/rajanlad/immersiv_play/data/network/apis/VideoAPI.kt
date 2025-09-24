package com.rajanlad.immersiv_play.data.network.apis

import com.rajanlad.immersiv_play.data.network.models.Video_Source
import retrofit2.Response
import retrofit2.http.GET

interface VideoAPI {
    @GET("https://hls-video-samples.r2.immersiv.cloud/videos.json")
    suspend fun getVideos(): Response<List<Video_Source>>
}