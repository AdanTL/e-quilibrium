package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Results extends AppCompatActivity {
    private TextView txtResults;
    private Map<String, String> results;
    private Map<String, String> resultsSorted;
    private String strResults = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        txtResults = (TextView)findViewById(R.id.resultText);

        results = (HashMap<String, String>)this.getIntent().getSerializableExtra("RESULTS");
        resultsSorted = new TreeMap<>(results);

        for (Map.Entry<String, String> e: resultsSorted.entrySet()) {
            strResults += "["+e.getKey() + "=" + e.getValue()+"]\n";
        }

        txtResults.setText(strResults);
    }
}