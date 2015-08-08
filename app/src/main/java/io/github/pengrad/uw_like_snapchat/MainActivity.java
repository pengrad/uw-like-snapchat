package io.github.pengrad.uw_like_snapchat;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener, Camera.PictureCallback {

    public static final int COLOR_CONDITION = 100;

    private Camera mCamera;

    private View mTransparentView;
    private Button mButtonStart;
    private Button mButtonTakePhoto;
    private View mTextTitle;

    private PixelCalculator mPixelCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);

        TextureView textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        mTransparentView = findViewById(R.id.transparentView);
        mTextTitle = findViewById(R.id.textTitle);

        mButtonStart = (Button) findViewById(R.id.buttonStart);
        mButtonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        mButtonStart.setOnClickListener(this);
        mButtonTakePhoto.setOnClickListener(this);

        mPixelCalculator = new PixelCalculator(COLOR_CONDITION);
    }

    private Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.d("+++++++", "Camera failed to open: " + e.getLocalizedMessage(), e);
                }
            }
        }

        return cam;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = openFrontFacingCamera();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mButtonStart.getId()) {
            mTransparentView.setVisibility(View.GONE);
            mTextTitle.setVisibility(View.GONE);
            mButtonStart.setVisibility(View.GONE);
            mButtonTakePhoto.setVisibility(View.VISIBLE);
        } else if (v.getId() == mButtonTakePhoto.getId()) {
            mButtonTakePhoto.setEnabled(false);
            mCamera.takePicture(null, null, this);
        }
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {
        mButtonTakePhoto.setEnabled(true);
        camera.startPreview();
        new AsyncTask<Void, BitmapColorInfo, BitmapColorInfo>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Analyzing pixels...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected BitmapColorInfo doInBackground(Void... params) {
                return mPixelCalculator.getBitmapColorInfo(data);
            }

            @Override
            protected void onPostExecute(BitmapColorInfo bitmapColorInfo) {
                progressDialog.dismiss();
                startActivity(DataActivity.newIntent(MainActivity.this, bitmapColorInfo));
            }
        }.execute();
    }
}
