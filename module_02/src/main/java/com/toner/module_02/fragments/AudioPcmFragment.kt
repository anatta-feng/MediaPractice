package com.toner.module_02.fragments

import android.Manifest
import android.media.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.toner.commom.BaseFragment
import com.toner.module_02.*
import com.toner.module_02.databinding.FragmentAudioPcmBinding
import com.toner.module_02.pojo.RecordPojo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AudioPcmFragment : BaseFragment() {
    private lateinit var record: AudioRecord
    private lateinit var dataBinding: FragmentAudioPcmBinding
    private lateinit var recordBuffer: ByteArray
    private val recordPojo = RecordPojo()
    private lateinit var recordFileName: String

    private lateinit var playBuffer: ByteArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_audio_pcm,
            container,
            false
        )
        dataBinding.setRecord(recordPojo)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        initView()
        requestPermission(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun initValue() {
        recordFileName = context!!.getExternalFilesDir("MediaPractice")!!
            .absolutePath + "/" + "testRecord.pcm"
    }

    private fun initView() {
        dataBinding.record.setOnClickListener {
            initRecord()
            record.startRecording()
            recordPojo.isRecording.set(true)
            startRecord()
        }
        dataBinding.stopRecord.setOnClickListener {
            recordPojo.isRecording.set(false)
            record.apply {
                stop()
                release()
            }
        }
        dataBinding.play.setOnClickListener {
            val minBufferSize =
                AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN_CONFIG, AUDIO_FORMAT)
            val audioTrack = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(AUDIO_FORMAT)
                    .setChannelMask(CHANNEL_OUT_CONFIG)
                    .build(),
                minBufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
            audioTrack.play()

            val audioFile = File(recordFileName)
            val fileInputStream = FileInputStream(audioFile)
            Thread {
                val buffer = ByteArray(minBufferSize)
                while (fileInputStream.available() > 0) {
                    val read = fileInputStream.read(buffer)
                    if (read == AudioTrack.ERROR_INVALID_OPERATION ||
                        read == AudioTrack.ERROR_BAD_VALUE
                    ) {
                        continue
                    }
                    if (read != 0 && read != -1) {
                        audioTrack.write(buffer, 0, read)
                    }

                }
                audioTrack.stop()
                audioTrack.release()
            }.start()
        }
    }

    private fun startRecord() {
        Thread {
            val fileOutputStream = FileOutputStream(recordFileName)
            while (recordPojo.isRecording.get()) {
                val read = record.read(recordBuffer, 0, recordBuffer.size)
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    fileOutputStream.write(recordBuffer)
                }
            }
            fileOutputStream.flush()
            fileOutputStream.close()
        }.start()
    }

    private fun initRecord() {
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        recordBuffer = ByteArray(bufferSize)
        record =
            AudioRecord(
                MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                CHANNEL_IN_CONFIG, AUDIO_FORMAT, bufferSize
            )

    }
}