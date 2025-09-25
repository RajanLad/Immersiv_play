package com.rajanlad.immersiv_play.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajanlad.immersiv_play._usecases.FetchVideosUsecase
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchVideosUsecase: FetchVideosUsecase
) : ViewModel(){
    private  val TAG = "MainActivityViewModel"
    private val _videos = MutableStateFlow(emptyList<Video_Source>())
    val videos: StateFlow<List<Video_Source>> get() = _videos

    init {
        fetchVideos()
        Log.e(TAG, "LocalSpatialCapabilities.current.isSpatialUiEnabled : " )
    }

    private fun fetchVideos() {
        viewModelScope.launch {


            _videos.value = fetchVideosUsecase()
        }
    }

}