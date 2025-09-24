package com.rajanlad.immersiv_play.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.rajanlad.immersiv_play.R
import com.rajanlad.immersiv_play.data.network.models.Video_Source
import com.rajanlad.immersiv_play.presentation.ui.theme.Immersiv_playTheme

@Composable
fun VideoItem(videoSource: Video_Source,onClick:()->Unit){
    Row(modifier = Modifier.fillMaxWidth().clickable{onClick()}, verticalAlignment = Alignment.CenterVertically){
        Image(
            painter = rememberAsyncImagePainter(videoSource.thumbnail),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )

        Text(videoSource.title?:"", modifier = Modifier.padding(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPreview() {
    Immersiv_playTheme {
        VideoItem(Video_Source(),{})
    }
}