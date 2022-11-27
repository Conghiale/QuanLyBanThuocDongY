package com.example.quanlybanthuocdongy;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlybanthuocdongy.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private List<Account> accountList = new ArrayList<> ();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("accounts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate (getLayoutInflater ());
        setContentView (registerBinding.getRoot ());
        setTitle ("Register Account");

        getDataAccountFirebase();
    }

    private void getDataAccountFirebase() {
        myRef.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren ()) {
                    accountList.add (dataSnapshot.getValue (Account.class));
                }
                registerBinding.btnRegister.setOnClickListener (view -> createAccount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e ("TAG", "onCancelled: " + error);
            }
        });
    }

    private void createAccount() {
        boolean exists = false;
        String username = registerBinding.etUserName.getText ().toString ();
        String email = registerBinding.etEmail.getText ().toString ();
        String password = registerBinding.etPassword.getText ().toString ();
        String confirmPassword = registerBinding.etConfirmPassword.getText ().toString ();
        Account newAccount = null;

        if(username.isEmpty () || email.isEmpty () || password.isEmpty () || confirmPassword.isEmpty ()){
            exists = true;
            Toast.makeText (this, "Please complete all information", Toast.LENGTH_SHORT).show ();
        } else {
            newAccount = new Account (username, email, password);
            if (accountList.size () > 0){
                for (Account account : accountList) {
                    if (username.equals (account.getUsername ())){
                        exists = true;
                        Toast.makeText (this, "Account already exists", Toast.LENGTH_SHORT).show ();
                        break;
                    }
                }

                if (!password.equals (confirmPassword) && !exists){
                    exists = true;
                    Toast.makeText (this, "Invalid password", Toast.LENGTH_SHORT).show ();
                }
            }else{
                if (!password.equals (confirmPassword)) {
                    exists = true;
                    Toast.makeText (this, "Invalid password", Toast.LENGTH_SHORT).show ();
                }
            }
        }

        if (!exists){
            myRef.child (username).setValue (newAccount, (error, ref) -> {
                Toast.makeText (this, "registered successfully", Toast.LENGTH_SHORT).show ();
                registerBinding.etUserName.setText ("");
                registerBinding.etEmail.setText ("");
                registerBinding.etPassword.setText ("");
                registerBinding.etConfirmPassword.setText ("");
                finish ();
            });
        }
    }
}
