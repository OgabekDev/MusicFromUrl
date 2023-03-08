package dev.ogabek.urlmusic

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ogabek.urlmusic.ui.theme.UrlMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UrlMusicTheme {
                PlayMusic()
            }
        }
    }
}

@Composable
fun PlayMusic() {

    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource("https://xitlar.net/mp3/uzbek_mp3/konsta_-_dost.mp3")
    mediaPlayer.prepare()

    var isPlaying by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (isPlaying) {
                    mediaPlayer.pause()
                    isPlaying = false
                } else {
                    mediaPlayer.start()
                    isPlaying = true
                }
            },
            modifier = Modifier
                .width(250.dp)
        ) {
            Text(
                text = if (isPlaying) "Pause" else "Play"
            )
        }

        Button(
            onClick = {
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 20000)
            },
            modifier = Modifier
                .width(250.dp)
                .padding(top = 20.dp)
        ) {
            Text(
                text = "Next + 2sec"
            )
        }

    }


}