package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Measures extends AppCompatActivity {
    private TextView textX, textY, textZ;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Button btnOk;
    private LinkedHashMap<String, String> results;
    private long timeStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        btnOk = (Button) findViewById(R.id.buttonOk);

        results = new LinkedHashMap<>();

        timeStart = System.currentTimeMillis();

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
        sensorManager.registerListener(gyroListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            String strX = "X : " + x + " rad/s";
            String strY = "Y : " + y + " rad/s";
            String strZ = "Z : " + z + " rad/s";

            long timeNow = System.currentTimeMillis();

            results.put(String.valueOf(timeNow), String.valueOf(x));


            textX.setText(strX);
            textY.setText(strY);
            textZ.setText(strZ);
        }
    };
}