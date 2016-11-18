package com.app.sirdreadlocks.e_quilibrium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.provider.*;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.DashPathEffect;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Results extends AppCompatActivity {
    private ImageView imgRadar;
    private TextView txtOSI_AS, txtOSI_SD, txtAPI_AS, txtAPI_SD, txtMLI_AS, txtMLI_SD, txtTZ_A, txtTZ_B, txtTZ_C, txtTZ_D, txtTQ_I, txtTQ_II, txtTQ_III, txtTQ_IV;
    private Map<String, Double[]> results;
    private Map<String, Double[]> resultsSorted;
    private String strResults = "";
    private Test test;
    private float TZ_A ;
    private float TZ_B ;
    private float TZ_C ;
    private float TZ_D ;
    private float TQ_I ;
    private float TQ_II ;
    private float TQ_III;
    private float TQ_IV ;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private StorageReference storage;
    private Patient currentPat;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // get data from Intent
        results = (HashMap<String, Double[]>) this.getIntent().getSerializableExtra("RESULTS");
        currentPat = (Patient)getIntent().getSerializableExtra("PATIENT");

        // sort Map
        resultsSorted = new TreeMap<>(results);

        // layout references
        imgRadar = (ImageView) findViewById(R.id.imgRadar);
        txtOSI_AS = (TextView) findViewById(R.id.txtOSI_AS);
        txtOSI_SD = (TextView) findViewById(R.id.txtOSI_SD);
        txtAPI_AS = (TextView) findViewById(R.id.txtAPI_AS);
        txtAPI_SD = (TextView) findViewById(R.id.txtAPI_SD);
        txtMLI_AS = (TextView) findViewById(R.id.txtMLI_AS);
        txtMLI_SD = (TextView) findViewById(R.id.txtMLI_SD);
        txtTZ_A = (TextView) findViewById(R.id.txtTZ_A);
        txtTZ_B = (TextView) findViewById(R.id.txtTZ_B);
        txtTZ_C = (TextView) findViewById(R.id.txtTZ_C);
        txtTZ_D = (TextView) findViewById(R.id.txtTZ_D);
        txtTQ_I = (TextView) findViewById(R.id.txtTQ_I);
        txtTQ_II = (TextView) findViewById(R.id.txtTQ_II);
        txtTQ_III = (TextView) findViewById(R.id.txtTQ_III);
        txtTQ_IV = (TextView) findViewById(R.id.txtTQ_IV);

        // txt setters
        txtOSI_AS.setText(""+getOSI_AS()+"");
        txtAPI_AS.setText(""+getAPI_AS()+"");
        txtMLI_AS.setText(""+getMLI_AS()+"");
        txtOSI_SD.setText(""+getOSI_SD()+"");
        txtAPI_SD.setText(""+getAPI_SD()+"");
        txtMLI_SD.setText(""+getMLI_SD()+"");

        getZonesPer();
        getQuadrantPer();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference("/"+auth.getCurrentUser().getUid()+"/"+currentPat.getId());
        long date = System.currentTimeMillis();

        byte[] byteArray = getIntent().getByteArrayExtra("IMAGE");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        imgRadar.setImageBitmap(bmp);
        storage.child(date+".jpg").putBytes(byteArray);

        test = new Test("Default",getOSI_AS(),getOSI_SD(),getAPI_AS(),getAPI_SD(),getMLI_AS(),getMLI_SD(),TZ_A,TZ_B,TZ_C,TZ_D,TQ_I,TQ_II,TQ_III,TQ_IV);

        database = FirebaseDatabase.getInstance().getReference("/"+auth.getCurrentUser().getUid()+"/tests/"+currentPat.getId());

        database.child(""+date+"").setValue(test);


    }

    private float getOSI_AS(){
        float sumX = 0;
        float sumY = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumX += Math.pow(0-e.getValue()[0],2);
            sumY += Math.pow(0-e.getValue()[1],2);
        }
        return (float)Math.sqrt((sumX+sumY)/resultsSorted.size());
    }

    private float getOSI_SD(){
        float mDef = 0;
        float stdDev = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            mDef += Math.sqrt(Math.pow(e.getValue()[0],2)+Math.pow(e.getValue()[1],2));
        }
        mDef = mDef/resultsSorted.size();
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            stdDev += Math.sqrt(Math.pow(Math.sqrt(Math.pow(e.getValue()[0],2)+Math.pow(e.getValue()[1],2))-mDef,2));
        }
        return stdDev/resultsSorted.size();
    }

    private float getAPI_AS(){
        float sumY = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumY += Math.pow(0-e.getValue()[1],2);
        }
        return (float)Math.sqrt(sumY/resultsSorted.size());
    }

    private float getAPI_SD() {
        float mDef = 0;
        float stdDev = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            mDef += e.getValue()[1];
        }
        mDef = mDef / resultsSorted.size();
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            stdDev += Math.sqrt(Math.pow(e.getValue()[1] - mDef, 2));
        }
        return stdDev / resultsSorted.size();
    }

    private float getMLI_AS(){
        float sumX = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumX += Math.pow(0-e.getValue()[0],2);
        }
        return (float)Math.sqrt(sumX/resultsSorted.size());
    }

    private float getMLI_SD() {
        float mDef = 0;
        float stdDev = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            mDef += e.getValue()[0];
        }
        mDef = mDef / resultsSorted.size();
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            stdDev += Math.sqrt(Math.pow(e.getValue()[0] - mDef, 2));
        }
        return stdDev / resultsSorted.size();
    }

    private void getZonesPer(){
        int A=0, B=0, C=0, D=0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            Double dist = Math.sqrt(Math.pow(e.getValue()[0],2)+Math.pow(e.getValue()[1],2));
            if(dist<=5.0)
                A++;
            else if(dist<=10.0)
                B++;
            else if (dist<=15.0)
                C++;
            else
                D++;
        }
        TZ_A = (A*100)/((float) resultsSorted.size());
        TZ_B = (B*100)/((float) resultsSorted.size());
        TZ_C = (C*100)/((float) resultsSorted.size());
        TZ_D = (D*100)/((float) resultsSorted.size());

        txtTZ_A.setText("A: "+TZ_A+"");
        txtTZ_B.setText("B: "+TZ_B+"");
        txtTZ_C.setText("C: "+TZ_C+"");
        txtTZ_D.setText("D: "+TZ_D+"");
    }

    private void getQuadrantPer(){
        int A=0, B=0, C=0, D=0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            if(e.getValue()[0]>0 && e.getValue()[1]>0)
                A++;
            else if(e.getValue()[0]<0 && e.getValue()[1]>0)
                B++;
            else if (e.getValue()[0]<0 && e.getValue()[1]<0)
                C++;
            else
                D++;
        }
        TQ_I = (A*100)/((float) resultsSorted.size());
        TQ_II = (B*100)/((float) resultsSorted.size());
        TQ_III = (C*100)/((float) resultsSorted.size());
        TQ_IV = (D*100)/((float) resultsSorted.size());

        txtTQ_I.setText("I: "+TQ_I+"");
        txtTQ_II.setText("II: "+TQ_II+"");
        txtTQ_III.setText("III: "+TQ_III+"");
        txtTQ_IV.setText("IV: "+TQ_IV+"");
    }
}