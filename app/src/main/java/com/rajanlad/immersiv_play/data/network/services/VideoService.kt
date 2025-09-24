package com.rajanlad.immersiv_play.data.network.services

import com.rajanlad.immersiv_play.data.network.apis.VideoAPI
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoService @Inject constructor(private val videoAPI : VideoAPI) {
    suspend fun getVideos() : List<Video_Source>{
        return withContext(Dispatchers.IO){
            videoAPI.getVideos().body() ?: emptyList()
        }
    }
}