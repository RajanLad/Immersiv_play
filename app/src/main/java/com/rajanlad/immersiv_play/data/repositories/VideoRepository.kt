package com.rajanlad.immersiv_play.data.repositories

import com.rajanlad.immersiv_play.data.network.models.Video_Source
import com.rajanlad.immersiv_play.data.network.services.VideoService
import javax.inject.Inject

class VideoRepository @Inject constructor(private val videoService: VideoService) {
    suspend fun getVideos():List<Video_Source>{
        val vids = videoService.getVideos()
        if(vids.size != 0 ){
            return vids
        }
        else{
            // local db if present fetch from it but for now return empty list again
            return vids
        }
    }
}