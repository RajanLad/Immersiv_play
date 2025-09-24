package com.rajanlad.immersiv_play.presentation.composables

import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import com.rajanlad.immersiv_play.R

@Composable
fun VideoDialog(
    onDismissRequest: () -> Unit = {},
    videoUrl:String
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Configure the player
            // here I'm making the video loop
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(modifier = Modifier, contentAlignment = Alignment.BottomEnd ){
//                Text(
//                    text = "Close",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentSize(Alignment.Center)
//                        .zIndex(2.0f),
//                    textAlign = TextAlign.Center,
//                )

                Icon(
                    imageVector = Icons.Filled.Close,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .zIndex(2.0f).padding(10.dp)
                        .clickable{
                            onDismissRequest()
                        },
                    contentDescription = "",
                    tint = Color.Black
                )

//                Image(
//                    painter = painterResource(R.drawable.ic_launcher_background),
//                    contentDescription = null,
//                    modifier = Modifier.size(exoPlayer.videoSize.width.dp,exoPlayer.videoSize.height.dp))

                AndroidExternalSurface(modifier = Modifier.aspectRatio(16f / 9f),
                    onInit = {
                        onSurface { surface, _, _ ->
                            exoPlayer.setVideoSurface(surface)

                            surface.onDestroyed { exoPlayer.setVideoSurface(null) }
                        }
                    }
                )

            }

        }
    }
}

@Preview
@Composable
fun VideoDialogExample(){
    VideoDialog({},"")
}