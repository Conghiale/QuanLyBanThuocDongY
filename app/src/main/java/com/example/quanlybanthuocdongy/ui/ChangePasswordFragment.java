package com.example.quanlybanthuocdongy.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlybanthuocdongy.LoginActivity;
import com.example.quanlybanthuocdongy.NavActivity;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.databinding.FragmentChangePasswordBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding changePasswordBinding;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private TextView tvTitleDialog, tvMessageDialog;
    private Button btnDisagree, btnAgree;
    private String oldPassword, newPassword, confirmPassword;

    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ProgressDialog mLoadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        changePasswordBinding = FragmentChangePasswordBinding.inflate (inflater, container, false);
        return changePasswordBinding.getRoot ();
//        View view = inflater.inflate (R.layout.fragment_change_password, container, false);
//        initOject(view);
//        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        etOldPassword = changePasswordBinding.etOldPassword;
        etNewPassword = changePasswordBinding.etNewPassword;
        etConfirmPassword = changePasswordBinding.etConfirmPassword;
        mAuth = FirebaseAuth.getInstance ();
        mLoadingBar = new ProgressDialog (getContext ());

        changePasswordBinding.btnChangePassword.setOnClickListener (view1 -> checkCredentials ());
    }

    private void checkCredentials() {
        oldPassword = etOldPassword.getText ().toString ();
        newPassword = etNewPassword.getText ().toString ();
        confirmPassword = etConfirmPassword.getText ().toString ();

        if (oldPassword.isEmpty () || oldPassword.length ()<7)
            showError (etOldPassword, "Password must contain at least 7 characters!");
        else if (newPassword.isEmpty () || newPassword.length ()<7)
            showError (etOldPassword, "Password must contain at least 7 characters!");
        else if (confirmPassword.isEmpty () || confirmPassword.length ()<7)
            showError (etOldPassword, "Password must contain at least 7 characters!");
        else if (!confirmPassword.equals (newPassword))
            showError (etOldPassword, "Password is not valid!");
        else {
            openDialogChangePassword(Gravity.CENTER);
        }
    }

    private void openDialogChangePassword(int gravity) {
        final Dialog dialog = new Dialog (getContext ());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.layout_dialog);

        Window window = dialog.getWindow ();
        if (window == null)
            return ;

        window.setLayout (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes ();
        windowAttributes.gravity = gravity;
        window.setAttributes (windowAttributes);
        dialog.setCancelable (false);

        tvTitleDialog = dialog.findViewById (R.id.tvTitleDialog);
        tvMessageDialog = dialog.findViewById (R.id.tvMessageDialog);
        btnDisagree = dialog.findViewById (R.id.btnDisagree);
        btnAgree = dialog.findViewById (R.id.btnAgree);


        tvTitleDialog.setText ("Change Password");
        tvMessageDialog.setText ("Are you sure you want to change the password of the " + mAuth.getCurrentUser ().getEmail () + " account?");
        btnDisagree.setText ("Back");
        btnAgree.setText ("Change");

        btnDisagree.setOnClickListener (view -> {
            dialog.dismiss ();
            FragmentTransaction transaction = getActivity ().getSupportFragmentManager ().beginTransaction ();
            transaction.replace (R.id.fragment_content, new HomeFragment ());
            transaction.commit ();
            NavActivity.FRAGMENT_CURRENT = NavActivity.FRAGMENT_HOME;
        });
        btnAgree.setOnClickListener (view -> {
            dialog.dismiss ();

            mLoadingBar.setTitle ("Change Password");
            mLoadingBar.setMessage ("Please wait white check your credentials");
            mLoadingBar.setCanceledOnTouchOutside (false);
            mLoadingBar.show ();

            mUser = mAuth.getCurrentUser ();
            AuthCredential authCredential = EmailAuthProvider.getCredential (mUser.getEmail (), oldPassword);
            mUser.reauthenticate (authCredential).addOnSuccessListener (unused -> {
                changePassword(dialog);
            }).addOnFailureListener (e -> Toast.makeText (getContext (), e.getMessage (), Toast.LENGTH_SHORT).show ());
        });
        dialog.show ();
    }

    private void changePassword(Dialog dialog) {
        mUser.updatePassword (newPassword).addOnCompleteListener (task -> {
            if (task.isSuccessful ()){
                mLoadingBar.dismiss ();
                Toast.makeText (getContext (), "Change password successfully", Toast.LENGTH_SHORT).show ();

                tvTitleDialog.setText ("Restart Activity");
                tvMessageDialog.setText (mUser.getEmail () + " account's password has been changed successfully. Do you want to exit your current activities and log back in?");
                btnDisagree.setText ("No");
                btnAgree.setText ("Yes");

                btnDisagree.setOnClickListener (view -> {
                    dialog.dismiss ();
                    FragmentTransaction transaction = getActivity ().getSupportFragmentManager ().beginTransaction ();
                    transaction.replace (R.id.fragment_content, new HomeFragment ());
                    transaction.commit ();
                    NavActivity.FRAGMENT_CURRENT = NavActivity.FRAGMENT_HOME;
                });
                btnAgree.setOnClickListener (view -> {
                    dialog.dismiss ();
                    mAuth.signOut ();
                    Intent intent = new Intent (getContext (), LoginActivity.class);
                    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (intent);
                });
                dialog.show ();
            }
        }).addOnFailureListener (e -> Toast.makeText (getContext (), e.getMessage (), Toast.LENGTH_SHORT).show ());
    }

    private void showError(EditText input, String s){
        input.setError (s);
        input.requestFocus ();
    }
}