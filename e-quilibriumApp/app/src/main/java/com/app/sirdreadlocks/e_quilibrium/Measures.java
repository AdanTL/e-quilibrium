package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Measures extends AppCompatActivity {
    private TextView textX, textY;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private Button btnStart, btnEnd;
    private HashMap<String, Double[]> results;
    private AsyncTest asyncTask;
    private Double pitch = null, roll = null;
    private CanvasView mCanvasView;
    private Bitmap bmp;

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

        btnStart = (Button) findViewById(R.id.btnStart);
        btnEnd = (Button) findViewById(R.id.btnEnd);

        btnEnd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                asyncTask.cancel(true);

                Intent intent =
                        new Intent(Measures.this, Results.class);
                intent.putExtra("RESULTS",results);

                try {
                    //Write file
                    mCanvasView.setDrawingCacheEnabled(true);
                    bmp = mCanvasView.getDrawingCache();
                    String filename = "bitmap.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bmp.recycle();

                    intent.putExtra("IMAGE", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        asyncTask = new AsyncTest();

        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                asyncTask.execute();
            }
        });


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
                    textX.setText("X : " + pitch.floatValue() + " º");//pitch goes from -90 to 90
                    textY.setText("Y : " + roll.floatValue() + " º");//roll goes from -90 to 90

                }
            }
        }
    };

    private class AsyncTest extends AsyncTask<Void, Double, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {

            //Empty while to wait sensors' set up
            while (getPitch()==null || getRoll()==null){}

            for(int i=0; i<400; i++){
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
                Toast.makeText(Measures.this, "Test done!",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Measures.this, "Canceled!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private class AsyncCalib extends AsyncTask<Void, Double, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {

            //Empty while to wait sensors' set up
            while (getPitch()==null || getRoll()==null){}

            for(int i=0; i<60; i++){
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
                Toast.makeText(Measures.this, "Test done!",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Measures.this, "Canceled!",
                    Toast.LENGTH_SHORT).show();
        }

    }

}