package com.example.quanlybanthuocdongy;

import static java.util.Objects.requireNonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlybanthuocdongy.databinding.ActivityForgotPasswordBinding;
import com.example.quanlybanthuocdongy.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding forgotPasswordBinding;
    private EditText inputEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        forgotPasswordBinding =ActivityForgotPasswordBinding.inflate (getLayoutInflater ());
        setContentView (forgotPasswordBinding.getRoot ());
        setTitle ("Login Account");

        inputEmail = forgotPasswordBinding.etEmail;
        mAuth = FirebaseAuth.getInstance ();
        mLoadingBar = new ProgressDialog (this);

        forgotPasswordBinding.btnGetPassword.setOnClickListener (view -> checkCredentials());
    }

    private void checkCredentials() {
        String email = inputEmail.getText ().toString ();

        if (email.isEmpty () || !Patterns.EMAIL_ADDRESS.matcher (email).matches ())  // !email.contains ("@")
            showError (inputEmail, "Email is not valid!");
        else {
            mLoadingBar.setTitle ("Get Password");
            mLoadingBar.setMessage ("Please wait white check your credentials");
            mLoadingBar.setCanceledOnTouchOutside (false);
            mLoadingBar.show ();

            mAuth.sendPasswordResetEmail (email).addOnCompleteListener (task -> {
                if (task.isSuccessful ()){
                    mLoadingBar.dismiss ();
                    Toast.makeText (this, "Please check your email address!", Toast.LENGTH_SHORT).show ();
                    startActivity (new Intent (this, LoginActivity.class));
                    finish ();
                }else
                    Toast.makeText (this, "Enter correct your email!", Toast.LENGTH_SHORT).show ();
            }).addOnFailureListener (e ->Toast.makeText (this, e.getMessage (), Toast.LENGTH_SHORT).show ());
        }
    }

    private void showError(EditText input, String s){
        input.setError (s);
        input.requestFocus ();
    }
}