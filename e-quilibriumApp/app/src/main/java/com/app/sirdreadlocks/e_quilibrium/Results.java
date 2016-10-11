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

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

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
}