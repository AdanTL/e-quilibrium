package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Measures extends AppCompatActivity {
    private TextView textX, textY, textZ;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private Button btnOk;
    private HashMap<String, Double[]> results;
    private AsyncGet asyncTask;
    private Double pitch = null, roll = null, azimuth = null;
    private CanvasView mCanvasView;

    public void onCreate(Bundle savedInstanceState) {
        results = new HashMap<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        super.onCreate(savedInstanceState);
        //XML View
        setContentView(R.layout.activity_measures);
        mCanvasView = (CanvasView) findViewById(R.id.canvasView);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);

        btnOk = (Button) findViewById(R.id.buttonOk);



        btnOk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                asyncTask.cancel(true);

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
        asyncTask = new AsyncGet();
        asyncTask.execute();
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListener);
    }

    
    public Double getPitch(){
        return pitch;
    }
    public Double getRoll(){
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
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    //Conversion of Math.toDegrees is not so exact as I would like
                    //Pitch is negative to fit with horizontal orientation
                    pitch = - Math.toDegrees(orientation[1]);
                    roll = Math.toDegrees(orientation[2]);

                    //Show all data but only need pitch(rotation in X axis) and roll (rotation in Y)
                    textX.setText("X : " + pitch + " º");//pitch goes from -90 to 90
                    textY.setText("Y : " + roll + " º");//roll goes from -90 to 90

                }
            }
        }
    };

    private class AsyncGet extends AsyncTask<Void, Double, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {

            while (getPitch()==null || getRoll()==null){}

            for(int i=0; i<1000; i++){
                //get samples at 20HZ
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(getPitch(),getRoll());

                if(isCancelled())
                    break;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            results.put(String.valueOf(System.currentTimeMillis()), values);
            mCanvasView.setPoint(values[0].floatValue(),values[1].floatValue());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(Measures.this, "Done!",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Measures.this, "Canceled!",
                    Toast.LENGTH_SHORT).show();
        }

    }

}