package com.example.quanlybanthuocdongy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quanlybanthuocdongy.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Account;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private List<Account> accountList = new ArrayList<> ();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("accounts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        loginBinding =ActivityLoginBinding.inflate (getLayoutInflater ());
        setContentView (loginBinding.getRoot ());
        setTitle ("Login Account");

        loginBinding.tvRegister.setOnClickListener (view -> startActivity (new Intent (this, RegisterActivity.class)));

        getDataAccountFirebase();
    }

    private void getDataAccountFirebase() {
        myRef.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren ()) {
                    accountList.add (dataSnapshot.getValue (Account.class));
                }
                loginBinding.btnLogin.setOnClickListener (view -> loginAccount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e ("TAG", "onCancelled: " + error);
            }
        });
    }

    private void loginAccount() {
        boolean exists = true;
        String username = loginBinding.etUserName.getText ().toString ();
        String password = loginBinding.etPassword.getText ().toString ();

        if (username.isEmpty () || password.isEmpty ()){
            exists = false;
            Toast.makeText (this, "Please enter username and password", Toast.LENGTH_SHORT).show ();
        }else {
            if (accountList.size () > 0){
                for (Account account : accountList) {
                    if (!username.equals (account.getUsername ()) || !password.equals (account.getPassword ())){
                        exists = false;
                        Toast.makeText (this, "Invalid username or password", Toast.LENGTH_SHORT).show ();
                        break;
                    }
                }
            }
        }
        if (exists){
            Toast.makeText (this, "Login successful", Toast.LENGTH_SHORT).show ();
            startActivity (new Intent (this, NavActivity.class));
        }
    }
}