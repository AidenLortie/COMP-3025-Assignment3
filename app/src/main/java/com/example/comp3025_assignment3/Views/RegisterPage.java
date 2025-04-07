package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp3025_assignment3.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance(); //instance of firebase
        //test to make sure register works (it does)
        /*binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser("test@testing.ca", "password");
            }
        });*/

        binding.loginBtnRegister.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               finish();
           }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailRegister.getText().toString().trim();
                String confirmPassword = binding.passwordConfirm.getText().toString().trim();
                String password = binding.passwordRegister.getText().toString().trim();

                if (email.isEmpty()) { //no email entered
                    Toast.makeText(RegisterPage.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {//no password entered
                    Toast.makeText(RegisterPage.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//see if eamil pattern is correct
                    Toast.makeText(RegisterPage.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<6) {//password must be at least 6 cheracters long
                    Toast.makeText(RegisterPage.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {//check if passwords are the same
                    Toast.makeText(RegisterPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //go back to main activity or fail message

                        if(task.isSuccessful()){
                            Log.d("tag", "createUserWithEmail is a success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterPage.this, "registerUser Pass: "+user.getUid(), Toast.LENGTH_SHORT).show();

                            Intent intentObj = new Intent(getApplicationContext(), Login.class);
                            startActivity(intentObj);
                            finish();
                        }else{
                            Log.d("tag", "createUserWithEamil is a fail", task.getException());
                            Toast.makeText(RegisterPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}