package com.app.sirdreadlocks.e_quilibrium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.DashPathEffect;

import java.io.FileInputStream;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // get Map from Intent
        results = (HashMap<String, Double[]>) this.getIntent().getSerializableExtra("RESULTS");

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

        getZonesPer();
        getQuadrantPer();

        Bitmap bmp;
        String filename = getIntent().getStringExtra("IMAGE");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            imgRadar.setImageBitmap(bmp);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getOSI_AS(){
        float sumX = 0;
        float sumY = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumX += Math.pow(0-e.getValue()[0],2);
            sumY += Math.pow(e.getValue()[1],2);
        }
        return (float)Math.sqrt(sumX+sumY/resultsSorted.size());
    }

    private float getAPI_AS(){
        float sumY = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumY += Math.pow(e.getValue()[1],2);
        }
        return (float)Math.sqrt(sumY/resultsSorted.size());
    }

    private float getMLI_AS(){
        float sumX = 0;
        for (HashMap.Entry<String, Double[]> e : resultsSorted.entrySet()) {
            sumX += Math.pow(0-e.getValue()[0],2);
        }
        return (float)Math.sqrt(sumX/resultsSorted.size());
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
         txtTZ_A.setText("A: "+A+"");
         txtTZ_B.setText("B: "+B+"");
         txtTZ_C.setText("C: "+C+"");
         txtTZ_D.setText("D: "+D+"");
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
        txtTQ_I.setText("I: "+A+"");
        txtTQ_II.setText("II: "+B+"");
        txtTQ_III.setText("III: "+C+"");
        txtTQ_IV.setText("IV: "+D+"");
    }
}