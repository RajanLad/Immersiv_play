package com.rajanlad.immersiv_play.presentation.composables

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialExternalSurface
import androidx.xr.compose.subspace.StereoMode
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.*

@OptIn(ExperimentalComposeApi::class)
@Composable
fun SpatialVideoSurface(videoUri: Uri) {
    val context = LocalContext.current
    Subspace {
        SpatialExternalSurface(
            modifier = SubspaceModifier
                .width(1200.dp)
                .height(676.dp),
            stereoMode = StereoMode.Mono // or SideBySide / TopBottom if stereo
        ) {
            onSurfaceCreated { surface ->
                val exoPlayer = ExoPlayer.Builder(context).build()
                exoPlayer.setVideoSurface(surface)
                exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
                exoPlayer.prepare()
                exoPlayer.play()
            }
            onSurfaceDestroyed {
                // release your player
            }
        }
    }
}
