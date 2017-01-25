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

import com.app.sirdreadlocks.e_quilibrium.sensor.SensorFusion;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Measures extends AppCompatActivity implements SensorEventListener {
    private DrawerLayout drawerLayout;
    private TextView textX, textY, txtCountDown, txtIP;
    private SensorManager sensorManager;
    private Button btnCOB, btnStart, btnEnd;
    private HashMap<String, Double[]> results, calib;
    private AsyncTest asyncTest;
    private AsyncCalib asyncCalib;
    private Double cob_x = 0.0, cob_y = 0.0;
    private CanvasView mCanvasView;
    private Bitmap bmp;
    private boolean cdFinished;
    private Patient currentPat;
    private String type;
    private String msgLog = "";
    private SensorFusion sensorFusion;
    private ServerSocket httpServerSocket;

    public void onCreate(Bundle savedInstanceState) {

        //Sensors setters
        sensorFusion = new SensorFusion();
        sensorFusion.setMode(SensorFusion.Mode.FUSION);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registerSensorManagerListeners();

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

        //layout setters
        mCanvasView = (CanvasView) findViewById(R.id.canvasView);

        textX = (TextView) findViewById(R.id.txtX);
        textY = (TextView) findViewById(R.id.txtY);
        txtCountDown = (TextView) findViewById(R.id.txtCountDown);

        btnCOB = (Button) findViewById(R.id.btnCOB);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnEnd = (Button) findViewById(R.id.btnEnd);

        txtIP = (TextView) findViewById(R.id.txtIP);

        txtIP.setText(getIpAddress() + ":"
                + HttpServerThread.HttpServerPORT + "\n");

        HttpServerThread httpServerThread = new HttpServerThread();
        httpServerThread.start();

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
        registerSensorManagerListeners();

        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startTest();
            }
        });

        btnCOB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cobTest();
            }
        });

    }

    private void startTest(){
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

    private void cobTest(){
        cdFinished = false;
        mCanvasView.cleanRadar();
        calib = new HashMap<>();
        asyncCalib = new AsyncCalib();
        asyncCalib.execute();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtCountDown.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                txtCountDown.setText("done!");
                cdFinished=true;
            }

        }.start();
    }

    public void registerSensorManagerListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (httpServerSocket != null) {
            try {
                httpServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorFusion.setAccel(event.values);
                sensorFusion.calculateAccMagOrientation();
                break;

            case Sensor.TYPE_GYROSCOPE:
                sensorFusion.gyroFunction(event);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorFusion.setMagnet(event.values);
                break;
        }
        textX.setText(sensorFusion.getRoll()+"º");
        textY.setText(sensorFusion.getPitch()+"º");
    }

    public Double getY(){
        return sensorFusion.getPitch() - cob_y;
    }
    public Double getX(){
        return sensorFusion.getRoll() - cob_x;
    }

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

            //for(int i=0; i<60; i++){
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

    /*
    * SERVER
    *
    *
    * */

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    private class HttpServerThread extends Thread {

        static final int HttpServerPORT = 8888;

        @Override
        public void run() {
            Socket socket = null;

            try {
                httpServerSocket = new ServerSocket(HttpServerPORT);

                while(true){
                    socket = httpServerSocket.accept();

                    HttpResponseThread httpResponseThread =
                            new HttpResponseThread(
                                    socket);
                    httpResponseThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class HttpResponseThread extends Thread {

        Socket socket;

        HttpResponseThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader is;
            PrintWriter os;
            String request;

            try {
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                request = is.readLine();

                os = new PrintWriter(socket.getOutputStream(), true);

                String response =
                        "<html><head></head>" +
                                "<body>" +
                                "<h1>e-quilibrium</h1>" +
                                "<form action=\"Start\" method=\"post\">"+
                                "<button name=\"foo\" value=\"Start\">Start</button>"+
                                "</form>"+
                                "<form action=\"GetCOB\" method=\"post\">"+
                                "<button name=\"foo\" value=\"GetCOB\">GetCOB</button>"+
                                "</form>"+
                                "</body></html>";

                os.print("HTTP/1.0 200" + "\r\n");
                os.print("Content type: text/html" + "\r\n");
                os.print("Content length: " + response.length() + "\r\n");
                os.print("\r\n");
                os.print(response + "\r\n");
                os.flush();
                socket.close();

                msgLog = "Request: " + request + "\n";

                Measures.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String valueOf = "POST /";
                        String delimiter = " ";
                        String value = null;
                        if (msgLog.contains(valueOf)){
                            String newline =  msgLog.substring(msgLog.indexOf(valueOf) + valueOf.length() );
                            value  = newline.substring(0, newline.indexOf(delimiter));
                            switch (value.trim()) {
                                case "Start":
                                    startTest();
                                    break;
                                case "GetCOB":
                                    cobTest();
                                    break;
                            }
                        }
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return;
        }
    }

}