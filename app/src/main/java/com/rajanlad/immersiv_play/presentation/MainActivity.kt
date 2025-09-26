package com.rajanlad.immersiv_play.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.spatial.ContentEdge


import androidx.xr.compose.spatial.Orbiter
//import androidx.xr.compose.spatial.OrbiterEdge
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.ExperimentalSubspaceVolumeApi
import androidx.xr.compose.subspace.SpatialBox
import androidx.xr.compose.subspace.SpatialColumn
import androidx.xr.compose.subspace.SpatialExternalSurface
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SpatialRow
import androidx.xr.compose.subspace.StereoMode
import androidx.xr.compose.subspace.Volume
import androidx.xr.compose.subspace.layout.SpatialArrangement
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.fillMaxSize
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.scale
import androidx.xr.compose.subspace.layout.size
import androidx.xr.compose.subspace.layout.width
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity
import com.rajanlad.immersiv_play.R
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import com.rajanlad.immersiv_play.presentation.composables.ModelInsideVolume
import com.rajanlad.immersiv_play.presentation.composables.VideoDialog
import com.rajanlad.immersiv_play.presentation.composables.VideoItem
import com.rajanlad.immersiv_play.presentation.ui.theme.Immersiv_playTheme
import com.rajanlad.immersiv_play.presentation.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import java.nio.file.Paths
import java.util.Objects
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val mainActivityViewModel : MainActivityViewModel by viewModels()

        setContent {
            Immersiv_playTheme {
                val spatialConfiguration = LocalSpatialConfiguration.current
                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    Subspace {
                    MainContentForSpatialContent (videos = mainActivityViewModel.videos,
                        onRequestHomeSpaceMode = spatialConfiguration::requestHomeSpaceMode
                    )
                    }
                } else {
                    MainContent2D(viewModel = mainActivityViewModel,onRequestFullSpaceMode = spatialConfiguration::requestFullSpaceMode)
                }
            }
        }
    }
}


@SuppressLint("RestrictedApi")
@Composable
fun MainContent2D(viewModel: MainActivityViewModel,onRequestFullSpaceMode: () -> Unit) {
    Surface {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var listofvids = viewModel.videos.collectAsState()
            val openAlertDialog = remember { mutableStateOf(false) }
            val videoLink = remember { mutableStateOf("") }
            LazyColumn(modifier = Modifier.weight(4f)) {
                items(listofvids.value){
                    VideoItem(it) {
                        openAlertDialog.value = true
                        videoLink.value = it.url ?: ""
                    }
                }
            }

            when {
                openAlertDialog.value ->{
                    VideoDialog(
                        videoUrl = videoLink.value,
                        onDismissRequest = {
                            openAlertDialog.value = false
                        }
                    )
                }
            }
            if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                FullSpaceModeIconButton(
                    onClick = onRequestFullSpaceMode,
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.5f)
                )
            }
        }
    }
}


@Composable
fun MainContentForSpatialContent(
    modifier: Modifier = Modifier,
    videos: StateFlow<List<Video_Source>>,
    onRequestHomeSpaceMode: () -> Unit
) {
    val listofvids = videos.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val videoLink = remember { mutableStateOf("") }

    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()

    val session = LocalSession.current !!
    ModelInsideVolume(session = session)

    SpatialBox {
        // Main Panel content (e.g., video list)
        ListOfVideosForSpatialLayout(
            openAlertDialog = openAlertDialog,
            modifier,
            listofvids = listofvids,
            onRequestHomeSpaceMode = onRequestHomeSpaceMode,
            exoPlayer = exoPlayer,
            videoLink = { link ->
                videoLink.value = link
            }
        )

        if (openAlertDialog.value) {

            SpatialExternalSurface(
                modifier = SubspaceModifier
                    .width(680.dp)
                    .height(400.dp)
                    .offset(z = 400.dp),
                stereoMode = StereoMode.Mono
            ) {
                onSurfaceCreated { surface ->
                    exoPlayer.setVideoSurface(surface)
                    exoPlayer.setMediaItem(MediaItem.fromUri(videoLink.value))
                    exoPlayer.prepare()
                    exoPlayer.play()
                }
                onSurfaceDestroyed {
                    exoPlayer.release()
                }
            }


        }
    }
}

@Composable
fun ListOfVideosForSpatialLayout(openAlertDialog: MutableState<Boolean>,
                                 modifier: Modifier,
                                 listofvids:State<List<Video_Source>>,
                                 videoLink:(String)->Unit,
                                 onRequestHomeSpaceMode: () -> Unit,
                                 exoPlayer: ExoPlayer){
    if (!openAlertDialog.value) {
        SpatialPanel(
            SubspaceModifier

                .offset(z = 100.dp,y=-200.dp)
                .resizable()
                .movable(),
        ) {
            Surface {

                LazyColumn(modifier = modifier.size(400.dp)) {
                    items(listofvids.value) {
                        VideoItem(it) {
                            openAlertDialog.value = true
                            videoLink(it.url ?: "")
                        }
                    }
                }

                Orbiter(
                    position = ContentEdge.Top,
                    offset = 20.dp,
                    alignment = Alignment.End,
                    shape = SpatialRoundedCornerShape(CornerSize(28.dp))
                ) {
                    HomeSpaceModeIconButton(
                        onClick = onRequestHomeSpaceMode,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

        }


    }

    else{
        SpatialPanel(
            modifier = SubspaceModifier
                .offset(z = 200.dp)

                .fillMaxSize()
        ) {

            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                modifier = modifier
                    .padding(20.dp)
                    .offset(200.dp, 400.dp)
                    .clickable {
                        exoPlayer.release()
                        openAlertDialog.value = false
                    }
                    .background(Color.White, shape = CircleShape)
                    .zIndex(2.0f),  // Ensure the close button is on top
                tint = Color.Black
            )
        }


    }
}

@Composable
fun FullSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_full_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_full_space_mode)
        )
    }
}

@Composable
fun HomeSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_home_space_mode)
        )
    }
}

@PreviewLightDark
@Composable
fun My2dContentPreview() {
    val mainActivityViewModel : MainActivityViewModel = viewModel()
    Immersiv_playTheme {

    }
}

@Preview(showBackground = true)
@Composable
fun FullSpaceModeButtonPreview() {
    Immersiv_playTheme {

    }
}

@PreviewLightDark
@Composable
fun HomeSpaceModeButtonPreview() {
    Immersiv_playTheme {
        HomeSpaceModeIconButton(onClick = {})
    }
}