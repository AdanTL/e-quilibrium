package com.app.sirdreadlocks.e_quilibrium;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    private Button btnNewPat,btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Toast.makeText(getApplicationContext(), "Welcome again "+auth.getCurrentUser().getDisplayName()+"!", Toast.LENGTH_SHORT).show();
        } else {
            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER,
                                    AuthUI.FACEBOOK_PROVIDER)
                            .setTheme(R.style.GreenTheme).build(),
                    RC_SIGN_IN);
        }

        btnNewPat = (Button) findViewById(R.id.btnNewPat);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);


        btnNewPat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this, NewPatient.class);

                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivityForResult(
                                        AuthUI.getInstance().createSignInIntentBuilder()
                                                .setIsSmartLockEnabled(false)
                                                .setProviders(
                                                    AuthUI.EMAIL_PROVIDER,
                                                    AuthUI.GOOGLE_PROVIDER,
                                                    AuthUI.FACEBOOK_PROVIDER)
                                                .setTheme(R.style.GreenTheme).build(),
                                        RC_SIGN_IN);
                            }
                        });
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(
                                        AuthUI.EMAIL_PROVIDER,
                                        AuthUI.GOOGLE_PROVIDER,
                                        AuthUI.FACEBOOK_PROVIDER)
                                .setTheme(R.style.GreenTheme).build(),
                        RC_SIGN_IN);
            }
        }
    }

}