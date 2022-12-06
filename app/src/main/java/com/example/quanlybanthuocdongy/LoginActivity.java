package com.example.quanlybanthuocdongy;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlybanthuocdongy.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        loginBinding =ActivityLoginBinding.inflate (getLayoutInflater ());
        setContentView (loginBinding.getRoot ());
        setTitle ("Login Account");

        inputEmail = loginBinding.etEmail;
        inputPassword = loginBinding.etPassword;
        mAuth = FirebaseAuth.getInstance ();
        mLoadingBar = new ProgressDialog (this);

        loginBinding.tvForgotPassword.setOnClickListener (view -> startActivity (new Intent (this, ForgotPasswordActivity.class)));
        loginBinding.tvRegister.setOnClickListener (view -> startActivity (new Intent (this, RegisterActivity.class)));
        loginBinding.btnLogin.setOnClickListener (view -> checkCredentials());
    }

    private void checkCredentials() {
        String email = inputEmail.getText ().toString ();
        String password = inputPassword.getText ().toString ();

        if (email.isEmpty () || !Patterns.EMAIL_ADDRESS.matcher (email).matches ())  // !email.contains ("@")
            showError (inputEmail, "Email is not valid!");
        else if (password.isEmpty () || password.length ()<7)
            showError (inputPassword, "Password must contain at least 7 characters!");
        else {
            mLoadingBar.setTitle ("Login");
            mLoadingBar.setMessage ("Please wait white check your credentials");
            mLoadingBar.setCanceledOnTouchOutside (false);
            mLoadingBar.show ();

            mAuth.signInWithEmailAndPassword (email, password)
                    .addOnCompleteListener (task -> {
                        if (task.isSuccessful ()){
                            mLoadingBar.dismiss ();
                            Toast.makeText (this, "Successfully Login", Toast.LENGTH_SHORT).show ();
                            Intent intent = new Intent (this, NavActivity.class);
                            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity (intent);
                        }else {
                            mLoadingBar.dismiss ();
                            Toast.makeText (this, requireNonNull (task.getException ()).toString (), Toast.LENGTH_SHORT).show ();
                        }
                    }).addOnFailureListener (e -> Log.e ("TAG", "onFailure: " + e));
        }
    }

    private void showError(EditText input, String s){
        input.setError (s);
        input.requestFocus ();
    }
}