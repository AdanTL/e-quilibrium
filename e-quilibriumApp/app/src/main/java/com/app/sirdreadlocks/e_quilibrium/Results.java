package com.app.sirdreadlocks.e_quilibrium;

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
    private XYPlot plot;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // get Map from Intent
        results = (HashMap<String, Double[]>)this.getIntent().getSerializableExtra("RESULTS");

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        SimpleXYSeries series = new SimpleXYSeries("Series");

        // set domain and range
        plot.setDomainBoundaries(-50, 50, BoundaryMode.FIXED);
        plot.setRangeBoundaries(-50, 50, BoundaryMode.FIXED);

        //fill series
        for (Map.Entry<String, Double[]> e: results.entrySet()) {
            Double resValues[] = e.getValue();
            Double x = resValues[0];
            Double y = resValues[1];
            series.addLast(x,y);
        }

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
        seriesFormat.configure(getApplicationContext(),
                R.xml.point_formatter);


        // add each series to the xyplot:
        plot.addSeries(series, seriesFormat);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);


        results = (HashMap<String, Double[]>)this.getIntent().getSerializableExtra("RESULTS");
        resultsSorted = new TreeMap<>(results);

        for (Map.Entry<String, Double[]> e: resultsSorted.entrySet()) {
            strResults += "["+e.getKey() + "=>\t:" + e.getValue()[0] +", " + e.getValue()[1] + "\n";
        }
        txtResults = (TextView) findViewById(R.id.txtResults);
        txtResults.setMovementMethod(new ScrollingMovementMethod());
        txtResults.setText(strResults);

    }

}