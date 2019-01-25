package com.example.prune.fetedelascienceproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prune.fetedelascienceproject.map.mapController;
import com.example.prune.fetedelascienceproject.object.*;
import com.example.prune.fetedelascienceproject.Fragment.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {


        private static final String TAG = "EmailPassword";

        private EditText mEmailField;
        private EditText mPasswordField;
        private ProgressBar spinner;


        // [START declare_auth]
        private FirebaseAuth mAuth;
        // [END declare_auth]

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate: Avant le crash ?");
            setContentView(R.layout.activity_email_password);

            // Views
            mEmailField = findViewById(R.id.fieldEmail);
            mPasswordField = findViewById(R.id.fieldPassword);
            spinner = findViewById(R.id.progressBar);

            // Buttons
            findViewById(R.id.emailSignInButton).setOnClickListener(this);
            findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
            findViewById(R.id.signOutButton).setOnClickListener(this);
            findViewById(R.id.verifyEmailButton).setOnClickListener(this);
            getSupportActionBar().hide();

            // [START initialize_auth]
            // Initialize Firebase Auth
            Log.d("LEST TRY", "TRY TO GETINSTANCE");
            mAuth = FirebaseAuth.getInstance();
            Log.d("LEST TRY", "Did get instance");
            // [END initialize_auth]
        }

        // [START on_start_check_user]
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
          FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
        // [END on_start_check_user]

        private void createAccount(String email, String password) {
            Log.d(TAG, "createAccount:" + email);
            if (!validateForm()) {
                return;
            }
            //ProgressBar pb = new ProgressBar();
            //showProgressDialog();

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                           // hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END create_user_with_email]
        }

        private void signIn(String email, String password) {
            Log.d(TAG, "signIn:" + email);
            if (!validateForm()) {
                return;
            }

           // showProgressDialog();

            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                           // hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END sign_in_with_email]
        }

        private void signOut() {
            mAuth.signOut();
            updateUI(null);
        }

        private void sendEmailVerification() {
            // Disable button
            findViewById(R.id.verifyEmailButton).setEnabled(false);

            // Send verification email
            // [START send_email_verification]
            final FirebaseUser user = mAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // [START_EXCLUDE]
                            // Re-enable button
                            findViewById(R.id.verifyEmailButton).setEnabled(true);

                            if (task.isSuccessful()) {
                                Toast.makeText(EmailPasswordActivity.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(EmailPasswordActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END send_email_verification]
        }

        private boolean validateForm() {
            boolean valid = true;

            String email = mEmailField.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mEmailField.setError("Required.");
                valid = false;
            } else {
                mEmailField.setError(null);
            }

            String password = mPasswordField.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mPasswordField.setError("Required.");
                valid = false;
            } else {
                mPasswordField.setError(null);
            }

            return valid;
        }

        private void updateUI(FirebaseUser user) {
            //hideProgressDialog();
            if (user != null) {
                Intent main = new Intent(this,MainActivity.class);
                startActivityForResult(main,0);
            } else {
                findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
                findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
                findViewById(R.id.signedInButtons).setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                spinner.setVisibility(View.GONE);
            }
        }
    }
        @Override
        public void onClick(View v) {
            View view = this.getCurrentFocus();
            spinner.setVisibility(View.VISIBLE);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            int i = v.getId();
            if (i == R.id.emailCreateAccountButton) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            } else if (i == R.id.emailSignInButton) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            } else if (i == R.id.signOutButton) {
                signOut();
            } else if (i == R.id.verifyEmailButton) {
                sendEmailVerification();
            }
        }
}

