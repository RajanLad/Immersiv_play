package com.rajanlad.immersiv_play._usecases


import com.rajanlad.immersiv_play.data.network.models.Video_Source
import com.rajanlad.immersiv_play.data.repositories.VideoRepository
import javax.inject.Inject

class FetchVideosUsecase  @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke():List<Video_Source>{
        return videoRepository.getVideos()
    }
}