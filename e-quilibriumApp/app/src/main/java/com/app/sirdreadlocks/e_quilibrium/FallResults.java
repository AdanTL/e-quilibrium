package com.app.sirdreadlocks.e_quilibrium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FallResults extends AppCompatActivity {
    private ImageView imgRadar;
    private TextView txtOSI_AS,txtOSI_SD;
    private Map<String, Double[]> results;
    private Map<String, Double[]> resultsSorted;
    private Patient currentPat;
    protected HorizontalBarChart mChart;

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

        //Chart
        mChart = (HorizontalBarChart) findViewById(R.id.chart);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = mChart.getAxisLeft();
        //yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        //yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);


        BarDataSet setBar = (BarDataSet)mChart.getData().getDataSetByIndex(0);
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0.0f,2.0f));
        setBar.setValues(values);

        mChart.setFitBars(true);
        mChart.animateY(2500);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
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
