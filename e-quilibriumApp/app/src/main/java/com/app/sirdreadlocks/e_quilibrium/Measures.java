package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class Measures extends AppCompatActivity {
    private TextView textX, textY, textZ;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private Button btnOk;
    private HashMap<String, String> results;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        btnOk = (Button) findViewById(R.id.buttonOk);

        results = new HashMap<>();

        btnOk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent =
                        new Intent(Measures.this, Results.class);
                intent.putExtra("RESULTS",results);

                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListener);
    }

    public SensorEventListener sensorListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        float[] mGravity;
        float[] mGeomagnetic;
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    //Conversion of Math.toDegrees is not so exact as I would like
                    double azimuth = Math.toDegrees(orientation[0]);
                    double pitch = Math.toDegrees(orientation[1]);
                    double roll = Math.toDegrees(orientation[2]);

                    //Show all data but only need pitch(rotation in X axis)
                    textX.setText("A : " + azimuth + " º");
                    textY.setText("P : " + pitch + " º");//pitch goes from -90 to 90
                    textZ.setText("R : " + roll + " º");

                    //Save pitch in map with the current time
                    results.put(String.valueOf(System.currentTimeMillis()), String.valueOf(pitch));
                }
            }
        }
    };
}