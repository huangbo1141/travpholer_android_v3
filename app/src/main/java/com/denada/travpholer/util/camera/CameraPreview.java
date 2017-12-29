package com.denada.travpholer.util.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Size previewSize;
	private byte yuv[];
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// create the surface and start camera preview
			if (mCamera == null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
			}
		} catch (IOException e) {
			Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
		}
	}
	
	public void refreshCamera(Camera camera) {
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		
		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		// set preview size and make any resize, rotate or
		// reformatting changes here
		// start preview with new settings
		setCamera(camera);
		
		try {
			
			Parameters parameters = mCamera.getParameters();
			previewSize = parameters.getPreviewSize();
			
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setDisplayOrientation(90);
			mCamera.startPreview();
			
			yuv = new byte[getBufferSize()];
			mCamera.addCallbackBuffer(yuv);
			mCamera.setPreviewCallbackWithBuffer(new PreviewCallback() {
				public synchronized void onPreviewFrame(byte[] data, Camera c) {
					if (c != null) { 
						c.addCallbackBuffer(yuv);
					}
				}
			});
			
		} catch (Exception e) {
			Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		refreshCamera(mCamera);
	}

	public void setCamera(Camera camera) {
		//method to set a camera instance		
		mCamera = camera;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// mCamera.release();
		
	}
	
	private Size getBestPreviewSize(int width, int height, Parameters parameters) {
		Size result = null;
		for (Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return result;
	}
	
	private int getBufferSize() {
		int pixelformat = ImageFormat.getBitsPerPixel(mCamera.getParameters().getPreviewFormat());
		int bufSize = previewSize.width * previewSize.height * pixelformat / 8;
		return bufSize;
	}

	public Bitmap getBitmap()
			throws IOException {
		Bitmap bitmap = null;
		YuvImage yuvimage = new YuvImage(yuv, mCamera.getParameters().getPreviewFormat(), previewSize.width,
				previewSize.height, null);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, outStream);
		bitmap = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0,
				outStream.size());
		yuvimage = null;
		outStream = null;
		return bitmap;
	}	
}