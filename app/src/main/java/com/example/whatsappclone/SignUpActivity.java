package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsappclone.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    private EditText edUsername;
    private EditText edEmail;
    private EditText edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hiding the actionbar
        getSupportActionBar().hide();

        //Getting XML elements in java code
        edUsername = findViewById(R.id.edUserName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        //Declare the instance of firebase authentication and firebase database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account"); //Setting the title of progress dialog
        progressDialog.setMessage("We are creating your account"); //Setting the message for the dialog

        //Setting on click listener on signup btn
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting values from Edit texts
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                //Checking fields are not empty
                if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    edUsername.setError("Please enter username, email and password");
                    return;
                }

                if (username.isEmpty()) {
                    edPassword.setError("Please enter username");
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

                //Creating the new user with email and password
                auth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //Dismissing the dialog after authentication
                                    progressDialog.dismiss();


                                    //Creating instance of User
                                    Users users = new Users(username, email, password);

                                    //Getting id of user from auth result
                                    String id = task.getResult().getUser().getUid();

                                    //Saving the value in the database
                                    database.getReference().child("Users").child(id).setValue(users);

                                    Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //Navigating to signin activity
        TextView txAlreadyAccount = findViewById(R.id.txAlreadyAccount);
        txAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}