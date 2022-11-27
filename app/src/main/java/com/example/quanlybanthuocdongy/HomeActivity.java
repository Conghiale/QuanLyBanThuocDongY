package com.example.quanlybanthuocdongy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import view.MedicineAdapter;
import model.Medicine;
import room.MedicineDAO;
import room.MedicineDB;

public class HomeActivity extends AppCompatActivity implements MedicineAdapter.MedicineItemClickListener {

    private ActivityHomeBinding homeBinding;

    private List<Medicine> medicineList;
    private MedicineDB medicineDB;
    private MedicineDAO medicineDAO;

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("accounts");

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult (),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    // Handle the Intent
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate (getLayoutInflater ());
        setContentView (homeBinding.getRoot ());
        setTitle ("Home Medicine");

        recyclerView = findViewById (R.id.recyclerView);
        initOjects();
        loadMedicineList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMedicineList() {
        medicineDAO.getAllMedicineLive ().observe (this, medicineListSource -> {
            Collections.reverse (medicineListSource);
            medicineList.clear ();
            medicineList.addAll (medicineListSource);
            medicineAdapter.notifyDataSetChanged ();
        });
    }

    private void initOjects() {
        medicineList = new ArrayList<> ();
        medicineDB = MedicineDB.getInstance (this);
        medicineDAO = medicineDB.medicineDAO ();
        medicineAdapter = new MedicineAdapter (medicineList, this);
        medicineAdapter.setItemClickListener (this);

        GridLayoutManager layoutManager =new GridLayoutManager (this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager (layoutManager);
        recyclerView.setHasFixedSize (true);
        recyclerView.setAdapter (medicineAdapter);
    }

    @Override
    protected void onDestroy() {
        Log.e ("TAG", "onDestroy.");
        medicineDAO.deleteAllMedicine ();
        medicineDB.close ();
        super.onDestroy ();
    }

    @Override
    public void onClickMedicine(Medicine medicine, int position) {
        Intent intent = new Intent (this, ShowInfomationActivity.class);
        intent.putExtra ("Medicine", medicine);
        startActivity (intent);
    }
}