package com.example.heychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword,edtEmail;
    private Button btnSubmit;
    private TextView txtLogin,userName;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseDatabase DB = FirebaseDatabase.getInstance("https://heychat-3cb5e-default-rtdb.firebaseio.com/");

    private boolean isSigningUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.editEmail);
        edtPassword = findViewById(R.id.editPassword);
        edtUsername = findViewById(R.id.editUsername);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLogin = findViewById(R.id.txtLoginInfo);
        userName = findViewById(R.id.User);

        if (Auth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this,FriendsActivity.class));
            finish();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp){
                    if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Invalid Input! Please fill the missing fields" , Toast.LENGTH_SHORT).show();
                    }else{
                        handleSignUp();
                    }
                }else{
                    if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Invalid Input!" , Toast.LENGTH_SHORT).show();
                    }else{
                        handleLogin();
                    }
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp){
                    isSigningUp = false;
                    edtUsername.setVisibility(View.INVISIBLE);
                    userName.setVisibility(View.INVISIBLE);
                    btnSubmit.setText("Log In");
                    txtLogin.setText("Create an account: Sign Up");
                }else{
                    isSigningUp = true;
                    edtUsername.setVisibility(View.VISIBLE);
                    userName.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sign Up");
                    txtLogin.setText("Already have an account? Log In");
                }
            }
        });
    }
    private void handleLogin(){
        Auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,FriendsActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    };

    private void handleSignUp(){
        Auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                    DB.getReference("User/"+Auth.getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(),""));
                    startActivity(new Intent(MainActivity.this,FriendsActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    };

}