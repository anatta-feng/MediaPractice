package com.toner.module_04

import android.media.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.toner.commom.copyAssertsToData
import kotlinx.android.synthetic.main.activity_media_extractor.*
import java.nio.ByteBuffer

class MediaExtractorActivity : AppCompatActivity(), SurfaceHolder.Callback {

    companion object {
        private const val TAG = "MediaExtractorActivity"
    }

    private lateinit var oriVideoPath: String
    private lateinit var outputVideoOnlyPath: String

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_extractor)

        oriVideoPath = filesDir.absolutePath + "/movie.mp4"
        outputVideoOnlyPath = filesDir.absolutePath + "/movie_video_only.mp4"

        Thread {
            if (!::oriVideoPath.isInitialized) {
                oriVideoPath = filesDir.absolutePath + "/movie.mp4"
                outputVideoOnlyPath = filesDir.absolutePath + "/movie_video_only.mp4"
            }
            copyAssertsToData(this, "movie.mp4", oriVideoPath)
        }.start()


        surfaceView.holder.addCallback(this)

        start.setOnClickListener {
            startMediaCodec()
        }
        play_ori.setOnClickListener {
            playOriVideo()
        }
        play_new.setOnClickListener {
            playNewVideo()
        }
    }

    private fun playOriVideo() {
        Log.d(TAG, "start prepare: $oriVideoPath")
        releaseMediaPlayer()
        initMediaPlayer()
        mediaPlayer?.let {
            it.setDataSource(oriVideoPath)
            it.prepare()
        }

        Log.d(TAG, "prepare finish")
    }

    private fun playNewVideo() {
        Log.d(TAG, "start prepare")
        releaseMediaPlayer()
        initMediaPlayer()
        mediaPlayer?.let {
            it.setDataSource(outputVideoOnlyPath)
            it.prepareAsync()
        }
        Log.d(TAG, "prepare finish")
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.let {
            it.setDisplay(surfaceView.holder)
            it.setOnCompletionListener {
                Log.d(TAG, "OnCompletion")
//            it.stop()
            }

            it.setOnErrorListener { mp, what, extra ->
                Log.d(TAG, "OnError: what: $what; extra: $extra")
                false
            }

            it.setOnInfoListener { mp, what, extra ->
                Log.d(TAG, "OnInfo: what: $what; extra: $extra")
                false
            }

            it.setOnPreparedListener {
                Log.d(TAG, "OnPrepared")
                it.start()
            }
        }

    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.let {
            it.reset()
            it.release()
            mediaPlayer = null
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceCreated")
        mediaPlayer?.setDisplay(holder)
    }

    private fun startMediaCodec() {
        Thread {
            val mediaExtractor = MediaExtractor()
            mediaExtractor.setDataSource(oriVideoPath)

            var videoOnlyTackIndex = -1
            var frameRate = 0
            val mediaMuxer =
                MediaMuxer(outputVideoOnlyPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            for (trackIndex in 0 until mediaExtractor.trackCount) {
                val trackFormat = mediaExtractor.getTrackFormat(trackIndex)
                Log.d(TAG, "trackFormat: $trackFormat")
                val mime = trackFormat.getString(MediaFormat.KEY_MIME)
                if (mime == null || !mime.startsWith("video/")) {
                    continue
                }
                frameRate = trackFormat.getInteger(MediaFormat.KEY_FRAME_RATE)
                mediaExtractor.selectTrack(trackIndex)

                videoOnlyTackIndex = mediaMuxer.addTrack(trackFormat)
                mediaMuxer.start()
            }
            if (videoOnlyTackIndex == -1) {
                return@Thread
            }
            val bufferInfo = MediaCodec.BufferInfo()
            bufferInfo.presentationTimeUs = 0
            var sampleSize = 0
            val buffer = ByteBuffer.allocate(500 * 1024)
            val sampleTime = mediaExtractor.cachedDuration
            Log.d(TAG, "sampleTime: $sampleTime")
            while (mediaExtractor.readSampleData(buffer, 0).also {
                    sampleSize = it
                } > 0) {
                // 抛出问题，为什么每一帧的 size 不同？
                bufferInfo.size = sampleSize
                bufferInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
                bufferInfo.presentationTimeUs += 1000 * 1000 / frameRate
                Log.d(TAG, "process: ${bufferInfo.presentationTimeUs}")
                mediaMuxer.writeSampleData(videoOnlyTackIndex, buffer, bufferInfo)
                mediaExtractor.advance()
            }

            mediaExtractor.release()
            mediaMuxer.stop()
            mediaMuxer.release()
            Log.d(TAG, "already finish")
            runOnUiThread {
                Toast.makeText(this, "分离完成", Toast.LENGTH_SHORT).show()
                play_new.isEnabled = true
            }
        }.start()
    }
}
