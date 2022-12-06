package com.example.quanlybanthuocdongy.ui;

import static com.example.quanlybanthuocdongy.ui.ProductFragment.medicineVM;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.NavActivity;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.ShowInformationActivity;
import com.example.quanlybanthuocdongy.databinding.FragmentCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.Medicine;
import model.MedicineViewModel;
import view.MedicineCartAdapter;

public class CartFragment extends Fragment {

    private MedicineCartAdapter medicineCartAdapter;
    private RecyclerView recyclerView;
    private List<Medicine> medicineCartList;
    private List<Integer> listIDMedicineCart;
    private TextView tvTotal;
    private AppCompatButton btnBuy;
    private FragmentCartBinding cartBinding;

    private FirebaseAuth mAuth;
    private DatabaseReference myRefAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cartBinding = FragmentCartBinding.inflate (inflater, container, false);
        return cartBinding.getRoot ();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        recyclerView = view.findViewById (R.id.recyclerView);
        medicineCartList = new ArrayList<> ();
        listIDMedicineCart = new ArrayList<> ();
        mAuth = NavActivity.mAuth;
        myRefAccount = NavActivity.myRefAccount;
        tvTotal = cartBinding.tvTotal;
        btnBuy = cartBinding.btnBuyAllMedicineCart;
        medicineVM = new ViewModelProvider (this). get (MedicineViewModel.class);
        medicineCartAdapter = new MedicineCartAdapter (medicineCartList, getContext ());
        recyclerView.setAdapter (medicineCartAdapter);
        initObjects();
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void initObjects() {
        LinearLayoutManager layoutManager = new LinearLayoutManager (getContext (), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager (layoutManager);
        recyclerView.setHasFixedSize (true);

//        Get medicineCartList from medicine model
        medicineVM.getMedicineCartListLD ().observe (Objects.requireNonNull (getActivity ()), medicineCartList -> {
            Collections.reverse (medicineCartList);
            Log.e ("TAG", "medicineCartList1:");
            setMedicineCartList (medicineCartList);
            setTotal ();
            medicineCartAdapter.setMedicineCartList (medicineCartList);
        });

        medicineCartAdapter.setItemClickListener (new MedicineCartAdapter.MedicineCartItemClickListener () {
            @Override
            public void onClickMedicine(Medicine medicine, int position) {
                Intent intent = new Intent (getContext (), ShowInformationActivity.class);
                intent.putExtra ("Medicine", medicine);
                startActivity (intent);
            }

            @Override
            public void onClickRemoveToCart(Medicine medicine) {
                medicineCartList.remove (medicine);
                medicineCartAdapter.setMedicineCartList (medicineCartList);
                medicineVM.setMedicineCartList (medicineCartList);

//                Get listIDMedicineCart from medicine model
                medicineVM.getListIDMedicineCartLD ().observe (Objects.requireNonNull (getActivity ()), listIDMedicineCart -> {
                    Collections.reverse (listIDMedicineCart);
                    Log.e ("TAG", "CartFragment_onClickRemoveToCart_getListIDMedicineCartLD.");
                    setListIDMedicineCart (listIDMedicineCart);
                });

//                update listIDMedicineCart to firebase
                listIDMedicineCart.remove (Integer.valueOf(medicine.getId ()));
                medicineVM.setListIDMedicineCart (listIDMedicineCart);

                HashMap<String, Object> updateListIDMedicineCart = new HashMap<> ();
                updateListIDMedicineCart.put("listIDMedicineCart", listIDMedicineCart);
                myRefAccount.child (Objects.requireNonNull (mAuth.getUid ())).updateChildren (updateListIDMedicineCart, (error, ref) -> {
                    Log.e ("TAG", "update lisIDMedicineCart.");
                });

                setTotal ();
            }

            @Override
            public void onClickNegativeCount(Medicine medicine) {
                setTotal ();
            }

            @Override
            public void onClickPositionCount(Medicine medicine) {
                setTotal ();
            }
        });
    }

    public void setTotal(){
        Double combineTotal = 0.0;
        for (Medicine medicine : medicineCartList) {
            combineTotal += medicine.getTotal ();
        }
        tvTotal.setText (String.valueOf (combineTotal));
    }

    public void setListIDMedicineCart(List<Integer> listIDMedicineCart) {
        this.listIDMedicineCart = listIDMedicineCart;
    }

    public void setMedicineCartList(List<Medicine> medicineCartList) {
        this.medicineCartList = medicineCartList;
    }
}