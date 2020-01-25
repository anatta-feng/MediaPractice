package com.toner.module_02.pojo

import android.database.Observable
import androidx.databinding.ObservableBoolean

data class RecordPojo(var isRecording: ObservableBoolean = ObservableBoolean(false), var recordLength: Long = 0L)