package com.example.quanlybanthuocdongy;

import static java.util.Objects.requireNonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlybanthuocdongy.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Account;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding registerBinding;
    private EditText inputUsername, inputEmail, inputPassword, inputConformPassword;
    private FirebaseAuth mAuth;
        private ProgressDialog mLoadingBar;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("accounts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate (getLayoutInflater ());
        setContentView (registerBinding.getRoot ());
        setTitle ("Register Account");

        inputUsername = registerBinding.etUserName;
        inputEmail = registerBinding.etEmail;
        inputPassword = registerBinding.etPassword;
        inputConformPassword = registerBinding.etConfirmPassword;
        mAuth = FirebaseAuth.getInstance ();
        mLoadingBar = new ProgressDialog (this);

        registerBinding.tvAlreadyAccount.setOnClickListener (view -> startActivity (new Intent (this, LoginActivity.class)));
        registerBinding.btnRegister.setOnClickListener (view -> checkCredentials());
    }

    private void checkCredentials() {
        String username = inputUsername.getText ().toString ();
        String email = inputEmail.getText ().toString ();
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String password = inputPassword.getText ().toString ();
        String confirmPassword = inputConformPassword.getText ().toString ();

        if (username.isEmpty () || username.length ()<7)
            showError(inputUsername, "Your username is not valid!");
        else if (email.isEmpty () || !Patterns.EMAIL_ADDRESS.matcher (email).matches ())  // !email.contains ("@")
            showError(inputEmail, "Your email is not valid!");
        else if (password.isEmpty () || password.length ()<7)
            showError(inputPassword, "Password must contain at least 7 characters!");
        else if (confirmPassword.isEmpty () || !confirmPassword.equals (password))
            showError(inputConformPassword, "Password not match!");
        else {
            mLoadingBar.setTitle ("Registration");
            mLoadingBar.setMessage ("Please wait white check your credentials");
            mLoadingBar.setCanceledOnTouchOutside (false);
            mLoadingBar.show ();

            mAuth.createUserWithEmailAndPassword (email, password)
                    .addOnCompleteListener (task -> {
                        if (task.isSuccessful ()){
                            mLoadingBar.dismiss ();
                            Toast.makeText (this, "Successfully Registration", Toast.LENGTH_SHORT).show ();
                            createAccount();
                            Intent intent = new Intent (this, LoginActivity.class);
                            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity (intent);
                        }else{
                            mLoadingBar.dismiss ();
                            Toast.makeText (this, requireNonNull (task.getException ()).toString (), Toast.LENGTH_SHORT).show ();
                        }
                    })
                    .addOnFailureListener (e -> Log.e ("TAG", "onFailure: " + e));
        }
    }

    private void showError(EditText input, String s){
        input.setError (s);
        input.requestFocus ();
    }

    private void createAccount() {
        String username = registerBinding.etUserName.getText ().toString ();
        String email = registerBinding.etEmail.getText ().toString ();
        String password = registerBinding.etPassword.getText ().toString ();
        Account newAccount = new Account (username, email, password);

        myRef.child (mAuth.getCurrentUser ().getUid ()).setValue (newAccount, (error, ref) -> {
            Log.e ("TAG", "registered successfully on realtime database: " + error);
        });
    }
}
