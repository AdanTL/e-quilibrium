package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

public class Results extends AppCompatActivity {
    private TextView txtResults;
    private HashMap<Integer, String> results;
    private String strResults = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        txtResults = (TextView)findViewById(R.id.resultText);

        results = (HashMap<Integer, String>)this.getIntent().getSerializableExtra("RESULTS");


        for (Map.Entry<Integer, String> e: results.entrySet()) {
            strResults += "["+e.getKey() + "=" + e.getValue()+"]\n";
        }

        txtResults.setText(strResults);
    }
}