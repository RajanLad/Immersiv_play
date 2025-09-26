package com.rajanlad.immersiv_play.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.xr.scenecore.GltfModel
import com.rajanlad.immersiv_play._usecases.FetchVideosUsecase
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.xr.compose.platform.LocalSession

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchVideosUsecase: FetchVideosUsecase
) : ViewModel(){
    private  val TAG = "MainActivityViewModel"
    private val _videos = MutableStateFlow(emptyList<Video_Source>())
    val videos: StateFlow<List<Video_Source>> get() = _videos

//    private val _model = mutableStateOf<GltfModel?>(null)
//    val model: State<GltfModel?> = _model


    init {
        fetchVideos()
    }


    private fun fetchVideos() {
        viewModelScope.launch {

            _videos.value = fetchVideosUsecase()
        }
    }

}