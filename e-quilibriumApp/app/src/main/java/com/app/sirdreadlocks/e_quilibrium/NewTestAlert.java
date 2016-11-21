package com.app.sirdreadlocks.e_quilibrium;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Adán on 21/11/2016.
 */

public class NewTestAlert extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] items = {"Postural Stability", "Athlete Single Leg", "Fall Risk"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Select new test type")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(getContext(),Measures.class);
                        intent.putExtra("TYPE",items[item]);
                        startActivity(intent);
                    }
                });

        return builder.create();
    }
}
