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
    private ImageView imageView;
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

        //imageView = (ImageView) findViewById(R.id.imageView);

        Bitmap bmp;
        String filename = getIntent().getStringExtra("IMAGE");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bmp);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}