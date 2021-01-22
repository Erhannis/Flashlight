package com.erhannis.android.flashlight;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.security.Policy;

public class MainActivity extends Activity {
  private CameraManager mCameraManager;
  private String mCameraId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

    ToggleButton tbFlashlight = (ToggleButton) this.findViewById(R.id.tbFlashlight);

    if (!isFlashAvailable) {
      Toast.makeText(this, "No flash available", Toast.LENGTH_LONG);
      LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRoot);
      llRoot.removeAllViews();
    } else {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
        }
      }

      final Camera cam = Camera.open();
      final Camera.Parameters pOn = cam.getParameters();
      pOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      final Camera.Parameters pOff = cam.getParameters();
      pOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

      cam.setParameters(pOn);
      cam.startPreview();

      tbFlashlight.setChecked(true);
      tbFlashlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (isChecked) {
            cam.setParameters(pOn);
          } else {
            cam.setParameters(pOff);
          }
        }
      });
    }
  }
}
