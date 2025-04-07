package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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