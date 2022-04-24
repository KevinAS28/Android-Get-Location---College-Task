package com.kevinas28.getlocationexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private Button locationBtn;
    private TextView locationTV;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_PERMISSION_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           locationTV = findViewById(R.id.textview_location);
           locationBtn = findViewById(R.id.button_location);
           mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
           locationBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   getLocation();
               }
           });

    }

    private void getLocation() {
        if (ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSION_LOCATION);
        } else {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location == null) {
                                locationTV.setText("Location not found");
                                return;
                            }

                            String res = "Latitude: " + String.valueOf(location.getLatitude()) +
                                    "\nLongitude: " + String.valueOf(location.getLongitude());

                            locationTV.setText(res);

                            new FetchAddressTask(MainActivity.this, new FetchAddressTask.OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(String result) {
                                    locationTV.setText(result);
                                }
                            }).execute(location);
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    return;
                }
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}