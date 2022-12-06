package com.example.quanlybanthuocdongy;

import static com.example.quanlybanthuocdongy.ui.ProductFragment.medicineVM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlybanthuocdongy.databinding.ActivityInformationMedicineBinding;
import com.example.quanlybanthuocdongy.ui.HomeFragment;
import com.example.quanlybanthuocdongy.ui.ProductFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import model.Account;
import model.Medicine;

public class ShowInformationActivity extends AppCompatActivity {
    private ActivityInformationMedicineBinding informationMedicineBinding;
    private Medicine medicine = new Medicine ();
    private List<Medicine> medicineCartList;
    private List<Integer> listIDMedicineCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        informationMedicineBinding = ActivityInformationMedicineBinding.inflate (getLayoutInflater ());
        setContentView (informationMedicineBinding.getRoot ());
        setTitle ("Show Information Medicine");

        Intent intent = getIntent ();
        medicine = intent.getParcelableExtra ("Medicine");
        medicineCartList = new ArrayList<> ();
        listIDMedicineCart = new ArrayList<> ();

        loadInformation();
        handleClickAddToCat();
    }

    private void handleClickAddToCat() {

            if (medicine.isAddToCart ()){
                informationMedicineBinding.btnAddToCart.setBackgroundResource (R.drawable.bg_green_animation_end);
                Toast.makeText (this, "This product has been added", Toast.LENGTH_SHORT).show ();
            }else{
                informationMedicineBinding.btnAddToCart.setOnClickListener (view -> {
                    informationMedicineBinding.btnAddToCart.setBackgroundResource (R.drawable.custom_button_green);
                    medicine.setAddToCart (true);
                    medicineVM.getMedicineCartListLD ().observe (this, medicineCartList -> {
                        this.medicineCartList = medicineCartList;
                    });
                    medicineCartList.add (medicine);
                    medicineVM.setMedicineCartList (medicineCartList);

                    medicineVM.getListIDMedicineCartLD ().observe (this, listIDMedicineCart -> {
                        Log.e ("TAG", "handleClickAddToCat: " + listIDMedicineCart.toString ());
                        this.listIDMedicineCart = listIDMedicineCart;
                    });
                    listIDMedicineCart.add (medicine.getId ());
                    medicineVM.setListIDMedicineCart (listIDMedicineCart);
                    Toast.makeText (this, "Successfully added product", Toast.LENGTH_SHORT).show ();

    //                  Update listIDMedicineCart in firebase
                    HashMap<String, Object> updateListIDMedicineCart = new HashMap<> ();
                    updateListIDMedicineCart.put("listIDMedicineCart", listIDMedicineCart);
                    ProductFragment.myRefAccount.child (Objects.requireNonNull (ProductFragment.mAuth.getUid ())).updateChildren (updateListIDMedicineCart, (error, ref) -> {
                        Log.e ("TAG", "update lisIDMedicineCart.");
                    });
                    handleClickAddToCat ();
                });
            }
    }

    @SuppressLint("SetTextI18n")
    private void loadInformation() {

//        load image
        informationMedicineBinding.img.setImageBitmap (loadImage (medicine));

//        load Name
        informationMedicineBinding.tvNameMedicine.setText (medicine.getName ());
//        load ID
        informationMedicineBinding.tvIDMedicine.setText ("ID: " + medicine.getId ());
//        load Price
        informationMedicineBinding.tvPriceMedicine.setText (medicine.getPrice ());
//        load Information
        informationMedicineBinding.tvInfMedicine.setText (medicine.getInfMedicine ());
    }

    public Bitmap loadImage(Medicine medicine){
        try (InputStream stream = getAssets ().open (medicine.getImg ())) {
            Bitmap bitmap = BitmapFactory.decodeStream (stream);
            stream.close ();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace ();
            return BitmapFactory.decodeByteArray(medicine.getByteImage (), 0, medicine.getByteImage ().length);
        }
    }
}
