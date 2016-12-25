package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Measures extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView textX, textY, txtCountDown;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private Button btnCOB, btnStart, btnEnd;
    private HashMap<String, Double[]> results, calib;
    private AsyncTest asyncTest;
    private AsyncCalib asyncCalib;
    private Double pitch = null, roll = null, cob_x = 0.0, cob_y = 0.0;
    private CanvasView mCanvasView;
    private Bitmap bmp;
    private boolean cdFinished;
    private SharedPreferences pref;
    private Patient currentPat;
    private String type;

    public void onCreate(Bundle savedInstanceState) {

        //pref = PreferenceManager.getDefaultSharedPreferences(R.xml.preferences);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        super.onCreate(savedInstanceState);

        currentPat = (Patient) getIntent().getExtras().getSerializable("PATIENT");
        type = getIntent().getStringExtra("TYPE");
        Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
        //XML View
        
        setContentView(R.layout.activity_measures);

        //Setup Navigation View
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(type +" test");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        mCanvasView = (CanvasView) findViewById(R.id.canvasView);



        textX = (TextView) findViewById(R.id.txtX);
        textY = (TextView) findViewById(R.id.txtY);
        txtCountDown = (TextView) findViewById(R.id.txtCountDown);

        btnCOB = (Button) findViewById(R.id.btnCOB);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnEnd = (Button) findViewById(R.id.btnEnd);

        btnEnd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if(asyncTest != null)
                    asyncTest.cancel(true);

                Intent intent = null;

                switch (type){
                    case "Postural Stability":
                        intent = new Intent(Measures.this, PosturalResults.class);break;
                    case "Athlete Single Leg":
                        intent = new Intent(Measures.this, PosturalResults.class);break;
                    case "Fall Risk":
                        intent = new Intent(Measures.this, FallResults.class);break;
                }


                intent.putExtra("RESULTS",results);
                intent.putExtra("PATIENT",currentPat);

                mCanvasView.setDrawingCacheEnabled(true);
                bmp = mCanvasView.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();

                intent.putExtra("IMAGE",byteArray);
                intent.putExtra("TYPE",type);

                if(results !=null)
                    startActivity(intent);
                else
                    Toast.makeText(Measures.this,"No one test has done",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cdFinished = false;
                results = new HashMap<>();
                mCanvasView.cleanRadar();
                asyncTest = new AsyncTest();
                asyncTest.execute();
                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txtCountDown.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        txtCountDown.setText("done!");
                        cdFinished=true;
                    }

                }.start();
            }
        });

        btnCOB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCanvasView.cleanRadar();
                calib = new HashMap<>();
                asyncCalib = new AsyncCalib();
                asyncCalib.execute();
            }
        });




    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListener);
    }

    
    public Double getY(){
        return pitch - cob_y;
    }
    public Double getX(){
        return roll - cob_x;
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
                    pitch = Math.toDegrees(orientation[1]);
                    roll = Math.toDegrees(orientation[2]);

                    //Show all data but only need pitch(rotation in X axis) and roll (rotation in Y)
                    /*    textX.setText("X : " + pitch.floatValue() + " º");//pitch goes from -90 to 90
                        textY.setText("Y : " + roll.floatValue() + " º");//roll goes from -90 to 90*/
                        textX.setText("X : " + getX() + " º");//pitch with COB
                        textY.setText("Y : " + getY() + " º");//roll with COB
                }
            }
        }
    };

    private class AsyncTest extends AsyncTask<Void, Double, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {

            //Empty while to wait sensors' set up
            while (getY()==null || getX()==null){}

            //for(int i=0; i<400; i++){
            while(!cdFinished){
                //get samples at 20HZ
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(getX(),getY());

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
            while (getY()==null || getX()==null){}

            for(int i=0; i<60; i++){
                //get samples at 20HZ
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(getX(),getY());

                if(isCancelled())
                    break;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            calib.put(String.valueOf(System.currentTimeMillis()), values);
            mCanvasView.setPoint(values[0].floatValue(),values[1].floatValue());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(Measures.this, "Calibration done!",
                        Toast.LENGTH_SHORT).show();
                calculateCOB();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Measures.this, "Canceled!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void calculateCOB(){
        Double sumX=0.0, sumY=0.0;
        for (HashMap.Entry<String, Double[]> e : calib.entrySet()) {
            sumX += e.getValue()[0];
            sumY += e.getValue()[1];
        }
        cob_x += sumX / (float)(calib.size());
        cob_y += sumY / (float)(calib.size());
    }

}