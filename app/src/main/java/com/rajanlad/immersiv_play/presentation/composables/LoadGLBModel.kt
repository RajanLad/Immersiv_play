package com.rajanlad.immersiv_play.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.xr.compose.platform.LocalSession
import androidx.xr.scenecore.GltfModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import androidx.compose.runtime.State

//@Composable
//fun LoadGlbModel(): State<GltfModel?> {
////    var session = LocalSession.current!!
////    // State to hold the loaded model
////    val modelState = remember { mutableStateOf<GltfModel?>(null) }
////
////    LaunchedEffect(session) {
////        val future: ListenableFuture<GltfModel> = session.createGltfResourceAsync("football_field.glb")
////        future.addListener({
////            try {
////                val model = future.get()
////                modelState.value = model
////            } catch (e: Exception) {
////                e.printStackTrace()
////                modelState.value = null
////            }
////        }, MoreExecutors.directExecutor())
////    }
////
////    return modelState
//}