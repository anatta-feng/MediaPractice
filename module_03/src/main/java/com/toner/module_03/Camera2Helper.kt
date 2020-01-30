package com.toner.module_03

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.CameraCharacteristics.LENS_FACING
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.concurrent.locks.ReentrantLock

class Camera2Helper(var context: Context, surfaceView: SurfaceView) {

    companion object Builder {
        private const val TAG = "Camera2Helper"
    }

    lateinit var mCameraId: String
    var mSurfaceView: SurfaceView = surfaceView
    var cameraDevice: CameraDevice? = null
    lateinit var mImageReader: ImageReader
    lateinit var mBackgroundThread: HandlerThread
    lateinit var mBackgroundHandler: Handler

    lateinit var mPreviewRequestBuilder: CaptureRequest.Builder

    fun start() {
        if (cameraDevice != null) {
            Log.w(TAG, "Camera already created")
            return
        }
        startBackgroundThread()
        mSurfaceView.holder.addCallback(mSurfaceHolderCallback)
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        Log.d(TAG, "openCamera")
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        setUpCamera(cameraManager)
        cameraManager.openCamera(mCameraId, mCameraStateCallback, mBackgroundHandler)
    }

    private fun setUpCamera(cameraManager: CameraManager) {
        for (cameraId in cameraManager.cameraIdList) {
            if (configCamera(cameraManager, cameraId)) {
                return
            }
        }
    }

    private fun configCamera(cameraManager: CameraManager, cameraId: String): Boolean {
        val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
        val facing = cameraCharacteristics[LENS_FACING]
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
            // 不使用前置摄像头
            return false
        }
        val streamConfigurationMap =
            (cameraCharacteristics[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
                ?: return false)
        mImageReader = ImageReader.newInstance(
            mSurfaceView.width,
            mSurfaceView.height,
            ImageFormat.YUV_420_888,
            2
        )
        mImageReader.setOnImageAvailableListener(ImageReaderAvailableListenerImp(), mBackgroundHandler)
        mCameraId = cameraId
        return true
    }

    private fun createCameraPreviewSession(camera: CameraDevice) {
        mPreviewRequestBuilder = camera.createCaptureRequest(TEMPLATE_PREVIEW)
        mPreviewRequestBuilder.addTarget(mSurfaceView.holder.surface)
        mPreviewRequestBuilder.addTarget(mImageReader.surface)
        camera.createCaptureSession(
            listOf(
                mSurfaceView.holder.surface,
                mImageReader.surface
            ), mCameraSessionStateCallback, mBackgroundHandler
        )
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraThread")
        mBackgroundThread.start()
        mBackgroundHandler = Handler(mBackgroundThread.looper)
    }

    private val mSurfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            Log.d(TAG, "surfaceCreated")
            openCamera()
        }

    }

    private val mCameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "onOpened")
            createCameraPreviewSession(camera)
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
        }
    }

    private val mCameraSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.w(TAG, "onConfigureFailed")
        }

        override fun onConfigured(session: CameraCaptureSession) {
            Log.d(TAG, "onConfigured")
            session.setRepeatingRequest(
                mPreviewRequestBuilder.build(),
                object : CameraCaptureSession.CaptureCallback() {},
                mBackgroundHandler
            )
        }
    }

    private class ImageReaderAvailableListenerImp : ImageReader.OnImageAvailableListener {
        private lateinit var y: ByteArray
        private lateinit var u: ByteArray
        private lateinit var v: ByteArray
        private val lock = ReentrantLock()

        override fun onImageAvailable(reader: ImageReader) {
            val image = reader.acquireNextImage()
            if (image.format == ImageFormat.YUV_420_888) {
                val planes = image.planes
                lock.lock()
                if (!::y.isInitialized) {
                    y = ByteArray(planes[0].buffer.limit() - planes[0].buffer.position())
                    u = ByteArray(planes[1].buffer.limit() - planes[1].buffer.position())
                    v = ByteArray(planes[2].buffer.limit() - planes[2].buffer.position())

                }
                if (planes[0].buffer.remaining() == y.size) {
                    planes[0].buffer.get(y)
                    planes[1].buffer.get(u)
                    planes[2].buffer.get(v)
                    // 接下来通过转换，可以转换为 Bitmap 进行展示
                }
                lock.unlock()
            }
            image.close()
        }
    }

}