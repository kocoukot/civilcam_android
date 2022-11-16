package com.civilcam.alert_feature.map.content

import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
private fun AlertVideoContent(
	url: String
) {
	val exoPlayer = rememberExoPlayerWithLifecycle(url)
	
	val playerView = rememberPlayerView(exoPlayer)
	
	AndroidView(factory = { playerView }, update = {
		exoPlayer.volume = 1f
		exoPlayer.playWhenReady = true
	})
	
	DisposableEffect(key1 = true) {
		onDispose {
			exoPlayer.release()
		}
	}
}

@Composable
fun rememberExoPlayerWithLifecycle(
	reelUrl: String
): ExoPlayer {
	
	val context = LocalContext.current
	val exoPlayer = remember(reelUrl) {
		ExoPlayer.Builder(context).build().apply {
			videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
			repeatMode = Player.REPEAT_MODE_ONE
			setHandleAudioBecomingNoisy(true)
			val defaultDataSource = DefaultHttpDataSource.Factory()
			val source = ProgressiveMediaSource.Factory(defaultDataSource)
				.createMediaSource(MediaItem.fromUri(reelUrl))
			setMediaSource(source)
			prepare()
		}
	}
	var appInBackground by remember {
		mutableStateOf(false)
	}
	val lifecycleOwner = LocalLifecycleOwner.current
	DisposableEffect(key1 = lifecycleOwner, appInBackground) {
		val lifecycleObserver = getExoPlayerLifecycleObserver(exoPlayer, appInBackground) {
			appInBackground = it
		}
		lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
		}
	}
	return exoPlayer
}

fun getExoPlayerLifecycleObserver(
	exoPlayer: ExoPlayer, wasAppInBackground: Boolean, setWasAppInBackground: (Boolean) -> Unit
): LifecycleEventObserver = LifecycleEventObserver { _, event ->
	when (event) {
		Lifecycle.Event.ON_RESUME -> {
			if (wasAppInBackground) exoPlayer.playWhenReady = true
			setWasAppInBackground(false)
		}
		Lifecycle.Event.ON_PAUSE -> {
			exoPlayer.playWhenReady = false
			setWasAppInBackground(true)
		}
		Lifecycle.Event.ON_STOP -> {
			exoPlayer.playWhenReady = false
			setWasAppInBackground(true)
		}
		Lifecycle.Event.ON_DESTROY -> {
			exoPlayer.release()
		}
		else -> {}
	}
}

@Composable
fun rememberPlayerView(exoPlayer: ExoPlayer): PlayerView {
	val context = LocalContext.current
	val playerView = remember {
		PlayerView(context).apply {
			layoutParams = ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
			)
			useController = false
			resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
			player = exoPlayer
		}
	}
	DisposableEffect(key1 = true) {
		onDispose {
			playerView.player = null
		}
	}
	return playerView
}