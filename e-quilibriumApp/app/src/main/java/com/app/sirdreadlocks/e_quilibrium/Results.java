package com.app.sirdreadlocks.e_quilibrium;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.graphics.DashPathEffect;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Results extends AppCompatActivity {
    private TextView txtResults;
    private Map<String, Double[]> results;
    private Map<String, Double[]> resultsSorted;
    private String strResults = "";
    private CanvasView plot;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // get Map from Intent
        results = (HashMap<String, Double[]>)this.getIntent().getSerializableExtra("RESULTS");

        // sort Map
        resultsSorted = new TreeMap<>(results);

        // initialize our plot reference:
        plot = (CanvasView) findViewById(R.id.plot);

        // fill plot
        for (Map.Entry<String, Double[]> e: resultsSorted.entrySet()) {

            strResults += e.getKey() + "=>\t:" + e.getValue()[0] +", " + e.getValue()[1] + "\n";

            Double resValues[] = e.getValue();
            float x = resValues[0].floatValue();
            float y = resValues[1].floatValue();
            plot.setPoint(x,y);
        }

        txtResults = (TextView) findViewById(R.id.txtResults);
        txtResults.setMovementMethod(new ScrollingMovementMethod());
        txtResults.setText(strResults);

    }

/*    @Override
    protected void onResume() {
        super.onResume();
        // fill plot
        for (Map.Entry<String, Double[]> e: resultsSorted.entrySet()) {

            Double resValues[] = e.getValue();
            float x = resValues[0].floatValue();
            float y = resValues[1].floatValue();
            plot.setPoint(x,y);
        }
    }*/
}