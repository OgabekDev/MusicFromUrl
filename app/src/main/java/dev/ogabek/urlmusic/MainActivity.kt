package dev.ogabek.urlmusic

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import dev.ogabek.urlmusic.ui.theme.UrlMusicTheme
import kotlinx.coroutines.delay

var mediaPlayer: MediaPlayer? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UrlMusicTheme {
                AudioSection()
            }
        }
    }
}

@Composable
fun AudioSection() {

    var isPlaying by remember {
        mutableStateOf(mediaPlayer?.isPlaying ?: false)
    }

    var sliderPosition by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "1-dars. Ro'za",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color.Transparent)
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    isPlaying = if (isPlaying) {
                        if (mediaPlayer != null) {
                            if (mediaPlayer!!.isPlaying) {
                                mediaPlayer!!.pause()
                            }
                        }
                        false
                    } else {
                        try {
                            if (mediaPlayer == null) {
                                mediaPlayer = MediaPlayer()
                                mediaPlayer!!.setDataSource("https://xitlar.net/mp3/uzbek_mp3/konsta_-_dost.mp3")
                                mediaPlayer!!.prepareAsync()
                                mediaPlayer!!.setOnPreparedListener {
                                    it.start()
                                }
                                mediaPlayer?.setOnCompletionListener {
                                    isPlaying = false
                                    mediaPlayer?.release()
                                    mediaPlayer = null
                                }
                            } else {
                                mediaPlayer!!.start()
                            }
                        } catch (e: Error) {
                            print("HHIHAHDFAHSLDLADJLA:SJD:LASKJD:ALSJ:DAs")
                            e.printStackTrace()
                        }
                        true
                    }
                },
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .shadow(0.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = "Play or Pause Music",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(.8f)
            ) {
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                    },
                    valueRange = 0F..126000F,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFD69B57),
                        disabledThumbColor = Color(0xFFD69B57),
                        activeTrackColor = Color(0xFFD69B57),
                        inactiveTrackColor = Color(0xFFA4A4A4),

                        ),
                    onValueChangeFinished = {
                        seekTo(mediaPlayer, sliderPosition.toInt())
                    }
                )
            }

            Row(
                modifier = Modifier
                    .width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = audioTime(sliderPosition.toInt()),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 0.dp)
                )
            }

        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (mediaPlayer != null) {
                    isPlaying = false
                    mediaPlayer!!.pause()
                }
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (mediaPlayer != null) {
                    mediaPlayer!!.release()
                    mediaPlayer = null
                    isPlaying = false
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(sliderPosition, isPlaying) {
        if (isPlaying) {
            delay(1000L)
            if (sliderPosition >= 126000) {
                sliderPosition = 0f
            } else {
                sliderPosition += 1000L
            }
        }
    }

}

fun seekTo(mediaPlayer: MediaPlayer?, seconds: Int) {
    if (mediaPlayer != null) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(seconds)
        } else {
            mediaPlayer.start()
            mediaPlayer.seekTo(seconds)
            mediaPlayer.pause()
        }
    }
}

fun audioTime(milliSeconds: Int): String {
    var timerString = ""
    var secondString = ""

    val hours: Int = milliSeconds / (1000 * 60 * 60)
    val minutes: Int = (milliSeconds % (1000 * 60 * 60)) / (1000 * 60)
    val seconds: Int = ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000)

    if (hours > 0) {
        timerString = "$hours:"
    }

    if (seconds < 10) {
        secondString = "0$seconds"
    } else {
        secondString = "$seconds"
    }

    timerString = "$timerString$minutes:$secondString"
    return timerString

}