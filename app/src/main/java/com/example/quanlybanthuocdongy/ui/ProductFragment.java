package com.example.quanlybanthuocdongy.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.AnimationUtil;
import com.example.quanlybanthuocdongy.NavActivity;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.ShowInformationActivity;
import com.example.quanlybanthuocdongy.databinding.FragmentProductMedicineBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.Account;
import model.Medicine;
import model.MedicineViewModel;
import room.MedicineDAO;
import room.MedicineDB;
import view.MedicineAdapter;

public class ProductFragment extends Fragment{

    private List<Medicine> medicineList;
    private List<Medicine> medicineCartList;
    private MedicineDB medicineDB;
    public static MedicineDAO medicineDAO;

    private FragmentProductMedicineBinding productMedicineBinding;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    public static MedicineViewModel medicineVM;

    public static FirebaseAuth mAuth;
    public static DatabaseReference myRefAccount;
    public static Account account;
//    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
//    public static DatabaseReference myRefMedicine = database.getReference("medicines");

//    private FirebaseStorage storage = FirebaseStorage.getInstance();
//    private StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanthuocdongy.appspot.com/");
//    private StorageReference myRefStorage = storageRef.child ("images");
    private List<Integer> listIDMedicineCart;

    private HomeFragment homeFragment;
    private boolean isLoad = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        productMedicineBinding = FragmentProductMedicineBinding.inflate (inflater, container, false);
        return productMedicineBinding.getRoot ();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        searchView = productMedicineBinding.searchView;
        recyclerView = view.findViewById (R.id.recyclerView_nav);
        homeFragment = (HomeFragment) getParentFragment ();
        mAuth = NavActivity.mAuth;
        myRefAccount = NavActivity.myRefAccount;

        initObjects ();
        loadMedicineFromRoom ();
    }

    private void initObjects() {
        medicineList = new ArrayList<> ();
        listIDMedicineCart = new ArrayList<> ();
        medicineCartList = new ArrayList<> ();
        medicineVM = new ViewModelProvider (this).get (MedicineViewModel.class);
        medicineDB = MedicineDB.getInstance (getContext ());
        medicineDAO = medicineDB.medicineDAO ();
        medicineAdapter = new MedicineAdapter (medicineList, getContext ());

        GridLayoutManager layoutManager =new GridLayoutManager (getContext (), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager (layoutManager);
        recyclerView.setHasFixedSize (true);
        recyclerView.setAdapter (medicineAdapter);

        searchView.clearFocus ();
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void loadMedicineFromRoom() {
        medicineDAO.getAllMedicineLive ().observe (requireActivity (), medicineListSource -> {
            Collections.reverse (medicineListSource);
            medicineList.clear ();
            medicineList.addAll (medicineListSource);
            Log.e ("TAG", "ProductFragment_loadMedicineFromRoom: " + medicineList.size ());


//          Get information medicine from informationMedicine.txt in Asset
            for (Medicine medicine : medicineList) {
                if (medicine.getInfMedicine ().equals (""))
                    medicine.setInfMedicine (loadInformationMedicine (medicine.getName ()));
            }
            loadListIDMedicineCartFromFB ();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadListIDMedicineCartFromFB() {
//            Get Data listIDMedicineCart from realtime Database to check IDMedicineCart
        Log.e ("TAG", "ProductFragment_loadListIDMedicineCartFromFB: " + medicineList.size ());
        mAuth.addAuthStateListener (firebaseAuth -> {
            myRefAccount.get().addOnCompleteListener (task -> {
                if (task.isSuccessful ()) {
                    account = task.getResult ().child (Objects.requireNonNull (mAuth.getUid ())).getValue (Account.class);
                    if (account != null){
                        if (account.getListIDMedicineCart () != null){
                            listIDMedicineCart.addAll (account.getListIDMedicineCart ());
                            medicineVM.setListIDMedicineCart (listIDMedicineCart);
                        }
                        handleIDMedicineCart ();
                    }
//                    medicineVM.setMedicineList (medicineList);
                }
            }).addOnFailureListener (e -> Log.e ("TAG", "loadListIDMedicineCartFromFB: " + e.getMessage ()));
        });
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void handleIDMedicineCart() {
        if (getView () != null){
            medicineVM.getListIDMedicineCartLD ().observe (getViewLifecycleOwner (), listIDMedicineCart -> {
                Collections.reverse (listIDMedicineCart);
                setListIDMedicineCart (listIDMedicineCart);
                Log.e ("TAG", "ProductFragment_handleIDMedicineCart" + medicineList.size ());
                for (Medicine medicine : medicineList) {
                    if (listIDMedicineCart.contains (medicine.getId ()) && !medicine.isAddToCart ()) {
                        medicine.setAddToCart (true);
                        medicineCartList.add (medicine);
                    }else if (!listIDMedicineCart.contains (medicine.getId ()) && medicine.isAddToCart ()){
                        medicine.setAddToCart (false);
                    }
                }
                medicineVM.setMedicineList (medicineList);
                homeFragment.setCountMedicineInCart (medicineCartList.size ());
                medicineVM.setMedicineCartList (medicineCartList);
            });

            medicineVM.getMedicineListLD ().observe (getViewLifecycleOwner (), medicineList -> {
                Log.e ("TAG", "ProductFragment_getMedicineListLD");
                medicineAdapter.setMedicineList (medicineList);
                homeFragment.setCountMedicineInCart (medicineCartList.size ());
//                medicineVM.setCountMedicineCart (medicineCartList.size ());
//            if (!isLoad)
//                isLoad = true;
            });
//          even click
            setClickMedicine();

//            medicineVM.getMedicineListLD ().observe (getViewLifecycleOwner (), medicineList -> {
//                medicineAdapter.setMedicineList (medicineList);
//                homeFragment.setCountMedicineInCart (medicineCartList.size ());
////            isLoad = true;
//            });
        }
    }

    private void setClickMedicine() {
        medicineAdapter.setItemClickListener (new MedicineAdapter.MedicineItemClickListener () {
            @Override
            public void onClickMedicine(Medicine medicine, ImageView image) {

                Intent intent = new Intent (getContext (), ShowInformationActivity.class);
                intent.putExtra ("Medicine", medicine);
                startActivity (intent);
            }

            @Override
            public void onClickAddToCart(AppCompatButton btnAddToCart, Medicine medicine) {
                AnimationUtil.translateAnimation (homeFragment.getViewAnimation (), btnAddToCart, homeFragment.getViewEndAnimation (), new Animation.AnimationListener () {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        medicine.setAddToCart (true);
//                          btnAddToCart.setBackgroundResource (R.drawable.bg_green_animation_end);
                        medicineAdapter.notifyDataSetChanged ();

//                          add medicineCartList
                        medicineCartList.add (medicine);
                        medicineVM.setMedicineCartList (medicineCartList);
                        listIDMedicineCart.add (medicine.getId ());
                        medicineVM.setListIDMedicineCart (listIDMedicineCart);

//                          Update listIDMedicineCart in firebase
                        HashMap<String, Object> updateListIDMedicineCart = new HashMap<> ();
                        updateListIDMedicineCart.put("listIDMedicineCart", listIDMedicineCart);
                        myRefAccount.child (Objects.requireNonNull (mAuth.getUid ())).updateChildren (updateListIDMedicineCart, (error, ref) -> {
                            Log.e ("TAG", "update lisIDMedicineCart.");
                        });
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterList(String text) {
        List<Medicine> filteredMedicineList = new ArrayList<> ();
        for (Medicine medicine : medicineList) {
            if (medicine.getName ().toLowerCase ().contains (text.toLowerCase ()))
                filteredMedicineList.add (medicine);
        }

        if (filteredMedicineList.isEmpty ()){
            Toast.makeText (getContext (), "No data found", Toast.LENGTH_SHORT).show ();
        }
        else {
            Log.e ("TAG", "filterList: " + filteredMedicineList.size ());
        }
        medicineAdapter.setFilteredMedicineList (filteredMedicineList);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    public String loadInformationMedicine(String nameMedicine){
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder infMedicineBuilder = new StringBuilder ();
        try {
            inputStream = Objects.requireNonNull (getContext ()).getAssets().open ("information_medicines.txt");
            inputStreamReader = new InputStreamReader (inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine ()) != null){
                String[] strLine = line.split ("_");
                if(nameMedicine.equals (strLine[1])){
                    infMedicineBuilder.append (strLine[3].replace ("/n", "\n"));
                }
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return infMedicineBuilder.toString ();
    }

    public void setListIDMedicineCart(List<Integer> listIDMedicineCart) {
        this.listIDMedicineCart = listIDMedicineCart;
    }

    @Override
    public void onDestroy() {
        Log.e ("TAG", "onDestroy.");
//        medicineDB.close ();
        super.onDestroy ();
    }
}