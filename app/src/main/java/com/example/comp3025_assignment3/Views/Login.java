package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp3025_assignment3.MainActivity;
import com.example.comp3025_assignment3.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.registerPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentObj = new Intent(getApplication(), RegisterPage.class);
                startActivity(intentObj);
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            final String logEmail = String.valueOf(binding.emailLogin);
            final String logPassword = String.valueOf(binding.passwordLogin);
            @Override
            public void onClick(View view) {
                //singIn("test@testing.ca", "password");
                singIn(logEmail, logPassword);
            }
        });
    }
    private  void singIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //go back to main activity or fail message
                        if(task.isSuccessful()){
                            Log.d("tag", "Login is a success");
                            FirebaseAuth user = mAuth;

                            Toast.makeText(Login.this, "login Pass: "+user.getUid(), Toast.LENGTH_SHORT).show();

                            Intent intentObj = new Intent(getApplicationContext(), Login.class);
                            startActivity(intentObj);
                            finish();
                        }else{
                            Log.d("tag", "Login is a fail", task.getException());
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}