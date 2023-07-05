package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    private EditText edEmail;
    private EditText edPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Hiding the actionbar
        getSupportActionBar().hide();

        //Getting XML elements in java code
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        Button btnSignin = findViewById(R.id.btnSignin);


        //Declare the instance of firebase authentication and firebase database
        auth = FirebaseAuth.getInstance();

        //Creating the dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in"); //Setting the title of progress dialog
        progressDialog.setMessage("We are logging in to your account"); //Setting the message for the dialog

        //Setting click listener to sign in button
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting values from Edit texts
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                //Checking fields are not empty
                if (email.isEmpty() && password.isEmpty()) {
                    edPassword.setError("Please enter email and password");
                    return;
                }
                if (email.isEmpty()) {
                    edPassword.setError("Please enter email");
                    return;
                }
                if (password.isEmpty()) {
                    edPassword.setError("Please enter password");
                    return;
                }

                //Showing the dialog when clicked on signup button
                progressDialog.show();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //Saving to shared preferences
                            saveToSharePreferences(email, password);
                            //Dismissing the progress dialog
                            progressDialog.dismiss();

                            //Creating explicit intent
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

        //Navigating to signup activity
        TextView txClickSignup = findViewById(R.id.txClickSignup);
        txClickSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Method to save email password to shared preferences
    private void saveToSharePreferences(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("myChattingApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userEmail", email);
        editor.putString("userPassword", password);
        editor.apply();
    }
}