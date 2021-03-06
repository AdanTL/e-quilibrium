package com.app.sirdreadlocks.e_quilibrium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FallResults extends AppCompatActivity {
    private ImageView imgRadar;
    private TextView txtOSI_AS,txtOSI_SD;
    private Map<String, Double[]> results;
    private Map<String, Double[]> resultsSorted;
    private Patient currentPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_results);

        // get data from Intent
        results = (HashMap<String, Double[]>) this.getIntent().getSerializableExtra("RESULTS");
        currentPat = (Patient)getIntent().getSerializableExtra("PATIENT");
        byte[] byteArray = getIntent().getByteArrayExtra("IMAGE");

        // sort Map
        resultsSorted = new TreeMap<>(results);

        // layout references
        imgRadar = (ImageView) findViewById(R.id.imgRadar);
        txtOSI_AS = (TextView) findViewById(R.id.txtOSI_AS);
        txtOSI_SD = (TextView) findViewById(R.id.txtOSI_SD);

        // layout setters
        txtOSI_AS.setText(""+getOSI_AS()+"");
        txtOSI_SD.setText(""+getOSI_SD()+"");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        imgRadar.setImageBitmap(bmp);
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
}
