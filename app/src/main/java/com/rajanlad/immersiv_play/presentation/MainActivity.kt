package com.rajanlad.immersiv_play.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.spatial.ContentEdge


import androidx.xr.compose.spatial.Orbiter
//import androidx.xr.compose.spatial.OrbiterEdge
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.width
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity
import com.rajanlad.immersiv_play.R
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import com.rajanlad.immersiv_play.presentation.composables.VideoDialog
import com.rajanlad.immersiv_play.presentation.composables.VideoItem
import com.rajanlad.immersiv_play.presentation.ui.theme.Immersiv_playTheme
import com.rajanlad.immersiv_play.presentation.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import java.nio.file.Paths
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
                        MySpatialContent(viewModel = mainActivityViewModel,
                            onRequestHomeSpaceMode = spatialConfiguration::requestHomeSpaceMode
                        )
                    }
                } else {
                    My2DContent(viewModel = mainActivityViewModel,onRequestFullSpaceMode = spatialConfiguration::requestFullSpaceMode)
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun MySpatialContent(viewModel: MainActivityViewModel,onRequestHomeSpaceMode: () -> Unit) {


    SpatialPanel(SubspaceModifier.width(1280.dp).height(800.dp).resizable().movable()) {
        Surface {
//            var session = LocalSession.current
//            val gltfModel = GltfModel.create(session =  , Paths.get("models", "saturn_rings.glb"))
            Row{
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(8.dp),  // Elevation gives the card shadow
                    shape = MaterialTheme.shapes.medium // Optional: define the card's shape (rounded corners)
                ) {
                    Text(
                        text = "This is 1/4 of the width",
                        modifier = Modifier.weight(3f),
                        fontSize = 16.sp
                    )
                }
                Column(modifier = Modifier.weight(3f)) {

                    Text(
                        text = "This is 3/4 of the width",
                        modifier = Modifier.weight(3f),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "This is 1/4 of the width",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
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

@SuppressLint("RestrictedApi")
@Composable
fun My2DContent(viewModel: MainActivityViewModel,onRequestFullSpaceMode: () -> Unit) {

    Surface {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            MainContent(modifier = Modifier.padding(10.dp).weight(4f),viewModel.videos)
//            if (LocalSession.current != null) {
            if (true) {
                FullSpaceModeIconButton(
                    onClick = onRequestFullSpaceMode,
                    modifier = Modifier.padding(10.dp).weight(0.5f)
                )
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, videos: StateFlow<List<Video_Source>>) {
    var listofvids = videos.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val videoLink = remember { mutableStateOf("") }
    LazyColumn(modifier = modifier) {
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

//@PreviewLightDark
//@Composable
//fun My2dContentPreview() {
//    val mainActivityViewModel : MainActivityViewModel = viewModel()
//    Immersiv_playTheme {
//        My2DContent(mainActivityViewModel)
//    }
//}

@Preview(showBackground = true)
@Composable
fun FullSpaceModeButtonPreview() {
    Immersiv_playTheme {

    }
}

//@PreviewLightDark
//@Composable
//fun HomeSpaceModeButtonPreview() {
//    Immersiv_playTheme {
//        HomeSpaceModeIconButton(onClick = {})
//    }
//}