package com.algogence.articleview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.sharp.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.algogence.articleview.ui.theme.RichTextComposerTheme


private val Int?.toMediaTime: String
    get() {
        if(this==null){
            return "00:00"
        }else{
            val t = this/1000
            var m = t/60
            var s = t%60
            var ms = if(m<10) "0$m" else m.toString()
            var ss = if(s<10) "0$s" else s.toString()
            return "$ms:$ss"
        }
    }

class AudioActivity : ComponentActivity() {
    private var visualizerView: AudioVisualizer? = null
    private var status = mutableStateOf(AudioPlayer.Status())
    private val progress = mutableStateOf(AudioPlayer.Progress())
    private val audioPlayer = AudioPlayer()

    private var title = ""
    private var description = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent?.getStringExtra("title")?:""
        description = intent?.getStringExtra("description")?:""
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setContent {
            RichTextComposerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black,

                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ){
                            IconButton(onClick = {
                                audioPlayer.stop()
                                finish()
                            }) {
                                Icon(
                                    imageVector = Icons.Sharp.ChevronLeft,
                                    tint = Color.Gray,
                                    contentDescription = "Back",
                                    modifier = Modifier.padding(24.dp).size(48.dp)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ){
                                Box(
                                    modifier = Modifier.padding(24.dp).fillMaxSize().weight(1f)
                                ){
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                    ){
                                        MarqueeText(
                                            title,
                                            gradientEdgeColor = Color.Black,
                                            color = Color.White,
                                            fontSize = 40.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            description,
                                            color = Color.Gray,
                                        )
                                    }

                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .clickable {
                                            changeVisualizerStyle()
                                        }
                                ){
                                    AndroidView(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.BottomCenter), // Occupy the max size in the Compose UI tree
                                        factory = { context ->
                                            AudioVisualizer(context).apply {
                                                layoutParams = ViewGroup.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                )
                                            }
                                        },
                                        update = { view ->
                                            this@AudioActivity.visualizerView = view
                                            view.bytes = progress.value.waveForm
                                        }
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ){
                                MediaSlider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .requiredHeight(40.dp)
                                        .padding(horizontal = 24.dp),
                                    padding = 19.dp,
                                    progress = progress.value.progressFactor,
                                    bufferProgress = status.value.bufferingProgress/100f,
                                    onSeek = {
                                        audioPlayer.seekToFactor(it)
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(end = 24.dp)
                                    .align(Alignment.End)
                            ){
                                Text(
                                    progress.value.progress.toMediaTime,
                                    color = Color.Gray
                                )
                                Text(
                                    "/",
                                    color = Color.Gray
                                )
                                Text(
                                    progress.value.total.toMediaTime,
                                    color = Color.Gray
                                )
                            }



                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ){
                                FloatingActionButton(
                                    onClick = {
                                        if(!(status.value.preparing&&!status.value.prepared)){
                                            playPause()
                                        }

                                    },
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .size(72.dp)
                                        .align(Alignment.Center),
                                    backgroundColor = Color(0xff242424)
                                ) {
                                    if(status.value.preparing&&!status.value.prepared){
                                        CircularProgressIndicator(
                                            color = Color.White
                                        )
                                    }
                                    else{
                                        Icon(
                                            imageVector = if(status.value.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                            tint = Color.White,
                                            contentDescription = "Play/Pause"
                                        )
                                    }
                                }

                                if(!(status.value.preparing&&!status.value.prepared)){
                                    FloatingActionButton(
                                        onClick = {
                                            rePlay()
                                        },
                                        modifier = Modifier
                                            .padding(24.dp)
                                            .size(48.dp)
                                            .align(Alignment.CenterEnd),
                                        backgroundColor = Color(0xff242424)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Replay,
                                            tint = Color.White,
                                            contentDescription = "Replay"
                                        )
                                    }

                                    FloatingActionButton(
                                        onClick = {
                                            stop()
                                        },
                                        modifier = Modifier
                                            .padding(24.dp)
                                            .size(48.dp)
                                            .align(Alignment.CenterStart),
                                        backgroundColor = Color(0xff242424)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Stop,
                                            tint = Color.White,
                                            contentDescription = "Stop"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun changeVisualizerStyle() {
        visualizerView?.cycleMode()
    }

    private fun stop() {
        audioPlayer.stop()
    }

    private fun rePlay() {
        startPlaying()
    }

    private fun playPause() {
        audioPlayer.playPause(intent?.getStringExtra("url"))
    }

    val AUDIO_PERMISSION_REQUEST_CODE = 102

    val WRITE_EXTERNAL_STORAGE_PERMS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    private fun checkPermissionsAndGo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                    startPlaying()
                }
            }.launch(
                arrayOf(Manifest.permission.RECORD_AUDIO)
            )
        } else {
            startPlaying()
        }
    }

    private fun startPlaying() {
        audioPlayer.play(intent?.getStringExtra("url"))
        audioPlayer.setListener {progress,status->
            this.progress.value = progress
            this.status.value = status
        }
    }

    override fun onDestroy() {
        audioPlayer.stop()
        super.onDestroy()
    }

    var resumeCount = 0
    override fun onResume() {
        super.onResume()
        if(++resumeCount==1){
            checkPermissionsAndGo()
        }
        else{
            audioPlayer.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayer.pause()
    }
}

@Composable
fun MediaSlider(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(40.dp),
    padding: Dp = 15.dp,
    bufferProgress: Float = 0f,
    progress: Float = 0f,
    onSeek: (Float)->Unit = {},
    backgroundColor: Color = Color.Gray,
    bufferColor: Color = Color(0xffff8282),
    progressColor: Color = Color.Red
) {
    Box(modifier = modifier
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(1) {
                    detectDragGestures { change, dragAmount ->
                        if (size.width == 0) {
                            onSeek(0f)
                        } else {
                            onSeek(change.position.x / size.width)
                        }
                    }
                }
                .pointerInput(1) {
                    detectTapGestures {
                        if (size.width == 0) {
                            onSeek(0f)
                        } else {
                            onSeek(it.x / size.width)
                        }
                    }
                }
                .padding(vertical = padding)
        ){

            val canvasWidth = size.width
            val canvasHeight = size.height
            drawLine(
                backgroundColor,
                Offset(0f,canvasHeight/2),
                Offset(canvasWidth,canvasHeight/2),
                strokeWidth = canvasHeight
            )
            drawLine(
                bufferColor,
                Offset(0f,canvasHeight/2),
                Offset(canvasWidth*bufferProgress,canvasHeight/2),
                strokeWidth = canvasHeight
            )
            drawLine(
                progressColor,
                Offset(0f,canvasHeight/2),
                Offset(canvasWidth*progress,canvasHeight/2),
                strokeWidth = canvasHeight
            )
        }
    }
}