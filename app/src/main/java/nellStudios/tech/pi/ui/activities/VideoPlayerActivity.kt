package nellStudios.tech.pi.ui.activities

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.C.CONTENT_TYPE_MOVIE
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.android.synthetic.main.activity_video_player.view.*
import kotlinx.android.synthetic.main.activity_video_player.view.exoPlayerView
import kotlinx.android.synthetic.main.exoplayer_custom_controls.*
import kotlinx.android.synthetic.main.exoplayer_custom_controls.view.*
import kotlinx.android.synthetic.main.exoplayer_custom_controls.view.episodeName
import kotlinx.android.synthetic.main.exoplayer_custom_controls.view.nextEpisode
import kotlinx.android.synthetic.main.exoplayer_custom_controls.view.previousEpisode
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.models.WatchedVideos
import nellStudios.tech.pi.viewmodels.PlayerViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity(), Player.EventListener, AudioManager.OnAudioFocusChangeListener, View.OnClickListener {

    private val args: VideoPlayerActivityArgs by navArgs()
    private val viewModel: PlayerViewModel by viewModels()

    private lateinit var videoUrl: String
    private lateinit var player: SimpleExoPlayer
    private lateinit var trackSelectionFactory: TrackSelection.Factory
    private var trackSelector: DefaultTrackSelector? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var content: Videos
    private var mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? = null
    private lateinit var audioManager: AudioManager
    private lateinit var mFocusRequest: AudioFocusRequest
    private val DEFAULT_MEDIA_VOLUME = 1f
    private val DUCK_MEDIA_VOLUME = 0.2f
    private lateinit var handler: Handler
    private var isFullScreen = false
    private var isVideoPlaying: Boolean = false

    private val speeds = arrayOf(0.25f, 0.5f, 1f, 1.25f, 1.5f, 2f)
    private val showableSpeed = arrayOf("0.25x", "0.50x", "1x", "1.25x", "1.50x", "2x")
    private var checkedItem = 2
    private var selectedSpeed = 2

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        setClickListeners()
        initializeAudioManager()
        initializePlayer()
        this.content = args.video
        updateContent(content)
        Log.i("PLAYER", args.video.videoUrl!!)
    }

    override fun onStart() {
        super.onStart()
        registerMediaSession()
    }

    override fun onDestroy() {
        player.release()
        if (::handler.isInitialized) {
            handler.removeCallbacksAndMessages(null)
        }
        super.onDestroy()
    }

    private fun initializePlayer() {
        exoPlayerFrameLayout.setAspectRatio(16f / 9f)
        trackSelectionFactory = AdaptiveTrackSelection.Factory()
        trackSelector = DefaultTrackSelector(trackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()

        player.playWhenReady = true
        player.audioAttributes = audioAttributes
        player.addListener(this)
        player.seekParameters = SeekParameters.CLOSEST_SYNC
        exoPlayerView.player = player
    }

    private fun setClickListeners() {
        exo_full_Screen.setOnClickListener(this)
        exo_track_selection_view.setOnClickListener(this)
        exo_speed_selection_view.setOnClickListener(this)
        back.setOnClickListener(this)
        nextEpisode.setOnClickListener(this)
        previousEpisode.setOnClickListener(this)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {


        val lastPath = uri.lastPathSegment
        val defaultDataSourceFactory = DefaultHttpDataSourceFactory("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")

        if(lastPath!!.contains("m3u8")){
            return HlsMediaSource.Factory(
                HlsDataSourceFactory {
                    val dataSource: HttpDataSource =
                        DefaultHttpDataSource("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")
                    dataSource.setRequestProperty("Referer", "https://vidstreaming.io/")
                    dataSource
                })
                .setAllowChunklessPreparation(true)
                .createMediaSource(uri)
        }else{
//            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(defaultDataSourceFactory)
            return ExtractorMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(uri)
        }

    }

    fun updateContent(content: Videos) {
        this.content = content
        episodeName.text = content.title
        exoPlayerView.videoSurfaceView.visibility =View.GONE

        this.content.nextVideo?.let {
            nextEpisode.visibility = View.VISIBLE
        } ?: kotlin.run {
            nextEpisode.visibility = View.GONE
        }
        this.content.previousVideo?.let {
            previousEpisode.visibility = View.VISIBLE
        } ?: kotlin.run {
            previousEpisode.visibility = View.GONE
        }
        if(!content.videoUrl.isNullOrEmpty()){
            updateVideoUrl(URLDecoder.decode(content.videoUrl, StandardCharsets.UTF_8.name()))
        }
    }

    private fun updateVideoUrl(videoUrl: String) {
        this.videoUrl = videoUrl
        loadVideo(seekTo = 0L)
    }

    private fun loadVideo(seekTo: Long? = 0, playWhenReady: Boolean = true) {
        val mediaSource = buildMediaSource(Uri.parse(videoUrl))
//        seekTo?.let {
//            player.seekTo(it)
//        }
        player.prepare(mediaSource, false, false)
        player.playWhenReady = playWhenReady
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.exo_track_selection_view -> {
                showDialog()
            }
            R.id.exo_speed_selection_view -> {
                showDialogForSpeedSelection()
            }
            R.id.exo_full_Screen -> {
                toggleFullView()
            }
            R.id.back -> {
//                (activity as VideoPlayerActivity).enterPipModeOrExit()
            }
            R.id.nextEpisode -> {
                playNextEpisode()
            }
            R.id.previousEpisode -> {
                playPreviousEpisode()
            }
        }
    }

    private fun toggleFullView() {
        if (isFullScreen) {
            exoPlayerFrameLayout.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            isFullScreen = false
            this?.let {
                exo_full_Screen.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.exo_controls_fullscreen_enter
                    )
                )
            }

        } else {
            exoPlayerFrameLayout.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            isFullScreen = true
            this?.let {
                exo_full_Screen.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.exo_controls_fullscreen_exit
                    )
                )
            }
        }
    }

    private fun refreshData() {
        if (::content.isInitialized && !content.videoUrl.isNullOrEmpty()) {
            loadVideo(player.currentPosition, true)
        } else {
//            refreshM3u8Url()
        }

    }

    private fun playNextEpisode() {
        playOrPausePlayer(playWhenReady = false, loseAudioFocus = false)
//        saveWatchedDuration()
        playNextEpisode()

    }

    private fun playPreviousEpisode() {
        playOrPausePlayer(playWhenReady = false, loseAudioFocus = false)
//        saveWatchedDuration()
        playPreviousEpisode()

    }

//    fun showLoading(showLoading: Boolean) {
//        if (::rootView.isInitialized) {
//            if (showLoading) {
//                rootView.videoPlayerLoading.visibility = View.VISIBLE
//            } else {
//                rootView.videoPlayerLoading.visibility = View.GONE
//            }
//        }
//    }

    private fun showDialog() {
        mappedTrackInfo = trackSelector?.currentMappedTrackInfo

        try {
            TrackSelectionDialogBuilder(
                this,
                getString(R.string.quality),
                trackSelector,
                0

            ).build().show()
        } catch (ignored: java.lang.NullPointerException) {
        }
    }

    // set playback speed for exoplayer
    private fun setPlaybackSpeed(speed: Float) {
        val params: PlaybackParameters = PlaybackParameters(speed)
        player.playbackParameters = params
    }

    // set the speed, selectedItem and change the text
    private fun setSpeed(speed: Int) {
        selectedSpeed = speed
        checkedItem = speed
        exo_speed_selection_view.text = showableSpeed[speed]
    }

    // show dialog to select the speed.
    private fun showDialogForSpeedSelection() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Set your playback speed")
            setSingleChoiceItems(showableSpeed, checkedItem) {_, which ->
                when (which) {
                    0 -> setSpeed(0)
                    1 -> setSpeed(1)
                    2 -> setSpeed(2)
                    3 -> setSpeed(3)
                    4 -> setSpeed(4)
                    5 -> setSpeed(5)
                }
            }
            setPositiveButton("OK") {dialog, _ ->
                setPlaybackSpeed(speeds[selectedSpeed])
                dialog.dismiss()
            }
            setNegativeButton("Cancel") {dialog, _ ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
        try {

            val videoQuality = trackSelections!!.get(0)!!.selectedFormat!!.height.toString() + "p"
            //TODO Change controls for quality
            exo_track_selection_view.text = videoQuality
        } catch (ignore: NullPointerException) {
        }

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        isVideoPlaying = playWhenReady
        if (playbackState == Player.STATE_READY  && playWhenReady) {
            exo_play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            exo_pause.setImageResource(R.drawable.ic_baseline_pause_24)
            playOrPausePlayer(true)

        }
        if (playbackState == Player.STATE_BUFFERING  && playWhenReady) {
            exo_play.setImageResource(0)
            exo_pause.setImageResource(0)
        }
        if (playbackState == Player.STATE_READY) {
            exoPlayerView.videoSurfaceView.visibility = View.VISIBLE
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initializeAudioManager() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val mAudioAttributes = android.media.AudioAttributes.Builder()
            .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MOVIE)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(mAudioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(true)
                .setOnAudioFocusChangeListener(this)
                .build()
        }

    }


    private fun requestAudioFocus(): Boolean {

        val focusRequest: Int

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (::audioManager.isInitialized && ::mFocusRequest.isInitialized) {
                focusRequest = audioManager.requestAudioFocus(mFocusRequest)
                checkFocusRequest(focusRequest = focusRequest)
            } else {
                false
            }

        } else {
            focusRequest = audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
            checkFocusRequest(focusRequest)
        }

    }

    private fun checkFocusRequest(focusRequest: Int): Boolean {
        return when (focusRequest) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> true
            else -> false
        }
    }

    private fun loseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(mFocusRequest)
        } else {
            audioManager.abandonAudioFocus(this)
        }
    }

    fun playOrPausePlayer(playWhenReady: Boolean, loseAudioFocus: Boolean = true) {
        if (playWhenReady && requestAudioFocus()) {
            player.playWhenReady = true
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            player.playWhenReady = false
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (loseAudioFocus) {
                loseAudioFocus()
            }
        }
    }

    override fun onStop() {
//        saveWatchedDuration()
//        if (::content.isInitialized) {
//            updateWatchedValue(content)
//        }
        playOrPausePlayer(false)
        saveWatchedDuration()
        unRegisterMediaSession()
        super.onStop()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                player.volume = DEFAULT_MEDIA_VOLUME
                playOrPausePlayer(true)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                playOrPausePlayer(false, loseAudioFocus = false)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                player.volume = DUCK_MEDIA_VOLUME
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                playOrPausePlayer(false)
            }
        }
    }

    private fun registerMediaSession() {
        mediaSession = MediaSessionCompat(this, "videoplayer")
//        if (::content.isInitialized) {
//
////            val mediaMetadataCompat = MediaMetadataCompat.Builder()
////                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, content.title)
////                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, resources.getString(R.string.app_name))
//////                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(resources, R.drawable.app_icon))
////                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, content.title)
////                    .build()
////
////            mediaSession.setMetadata(mediaMetadataCompat)
//        }
        mediaSession.isActive = true
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
    }

    private fun unRegisterMediaSession() {
        mediaSession.release()
        mediaSessionConnector.setPlayer(null)
    }

    fun saveWatchedDuration() {
        val watchedVideo = WatchedVideos().apply {
            video = args.video
            watchedDuration = player.currentPosition
            watchedPercentage = (player.currentPosition / player.duration) * 100
        }
        val modifiedUser = args.user.copy(
            watched = listOf(watchedVideo)
        )
        viewModel.saveWatchedDuration(modifiedUser)
        viewModel.successfull.observe(this, Observer {
            if (it) Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        })
    }

    fun isVideoPlaying(): Boolean{
        return isVideoPlaying
    }

}