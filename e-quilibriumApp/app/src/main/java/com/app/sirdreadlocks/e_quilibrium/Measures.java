package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private HashMap<String, HashMap<String, String>> results;
    public double pitch, roll, azimuth;

    public void onCreate(Bundle savedInstanceState) {
        results = new HashMap<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        super.onCreate(savedInstanceState);
        Plot plot = new Plot(this);
        setContentView(plot);

        //XML View
        /*setContentView(R.layout.activity_measures);



        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        btnOk = (Button) findViewById(R.id.buttonOk);



        btnOk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent =
                        new Intent(Measures.this, Results.class);
                intent.putExtra("RESULTS",results);

                startActivity(intent);
            }
        });*/
    }

    class Plot extends View{
        float x = 0;
        float y = 0;
        Path path = new Path();

        public Plot(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(6);
            path.moveTo(800,800);
            x = (float)getPitch();
            y = (float)getRoll();
            path.lineTo(x,y);
            canvas.drawCircle(800,800,400,paint);
            canvas.drawPath(path,paint);

        }





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

    public double getPitch(){
        return pitch;
    }
    public double getRoll(){
        return roll;
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
                    HashMap<String, String> pair = new HashMap<>();
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    //Conversion of Math.toDegrees is not so exact as I would like
                    azimuth = Math.toDegrees(orientation[0]);
                    pitch = Math.toDegrees(orientation[1]);
                    roll = Math.toDegrees(orientation[2]);

                    //Show all data but only need pitch(rotation in X axis) and roll (rotation in Y)
                    /*textX.setText("A : " + azimuth + " º");//useless for our purpose
                    textY.setText("P : " + pitch + " º");//pitch goes from -90 to 90
                    textZ.setText("R : " + roll + " º");//roll goes from -90 to 90*/

                    //Save a pair of data X & Y
                    pair.put(String.valueOf(pitch),String.valueOf(roll));

                    //Save pair in map with the current time
                    results.put(String.valueOf(System.currentTimeMillis()), pair);
                }
            }
        }
    };
}