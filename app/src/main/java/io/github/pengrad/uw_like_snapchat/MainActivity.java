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

    private Camera mCamera;

    private View mTansparentView;

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

        mTansparentView = findViewById(R.id.transparentView);
        mTansparentView.setVisibility(View.VISIBLE);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
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
        if (mTansparentView.getVisibility() == View.VISIBLE) {
            mTansparentView.setVisibility(View.GONE);
        } else {
            mCamera.takePicture(null, null, this);
        }
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {
        camera.startPreview();
        new AsyncTask<Void, Integer, Integer>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Analyzing pixels...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                return PixelCalculator.calcPixelsCount(data);
            }

            @Override
            protected void onPostExecute(Integer pixelsCount) {
                progressDialog.hide();
                startActivity(DataActivity.newIntent(MainActivity.this, pixelsCount));
            }
        }.execute();
    }


}
