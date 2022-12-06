package com.example.quanlybanthuocdongy.ui;

import static com.example.quanlybanthuocdongy.ui.ProductFragment.medicineVM;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlybanthuocdongy.NavActivity;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.databinding.FragmentCreateNewMedicineBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Medicine;

public class CreateNewMedicineFragment extends Fragment {

    private FragmentCreateNewMedicineBinding createNewMedicineBinding;
    private ImageView imageMedicine;
    private AppCompatButton btnChooseImageMedicine, btnCreateNewMedicine;
    private EditText etNameMedicine, etPriceMedicine, etInformationMedicine;
    private Uri uriFilePath;
    private List<Medicine> medicineList;
    private int ID = -1;

    private static final String PERM = Manifest.permission.READ_EXTERNAL_STORAGE;

    private ActivityResultLauncher<String>  requestPermissionLauncher = registerForActivityResult (new ActivityResultContracts.RequestPermission (), isGranted -> {
            if (isGranted)
                pickImageFromGallery ();
            else {
                Toast.makeText (requireContext (), "Can't open photo gallery because you don't give permission to read storage", Toast.LENGTH_LONG).show ();
            }
    });

    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent (),
            uri -> {
                uriFilePath = uri;
                imageMedicine.setImageURI (uri);
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        createNewMedicineBinding =  FragmentCreateNewMedicineBinding.inflate (inflater, container, false);
        return createNewMedicineBinding.getRoot ();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        imageMedicine = createNewMedicineBinding.imageMedicine;
        btnChooseImageMedicine = createNewMedicineBinding.btnChooseImageMedicine;
        etNameMedicine = createNewMedicineBinding.etNameMedicine;
        etPriceMedicine = createNewMedicineBinding.etPriceMedicine;
        etInformationMedicine = createNewMedicineBinding.etInformationMedicine;
        btnCreateNewMedicine = createNewMedicineBinding.btnCreateNewMedicine;
        medicineList = new ArrayList<> ();
        medicineVM.getMedicineListLD ().observe (requireActivity (), medicineList -> {
            this.medicineList = medicineList;
        });

        if (medicineList.size () > 0)
            ID = medicineList.get (medicineList.size ()-1).getId ()+1;
        else
            ID = 0;


        btnChooseImageMedicine.setOnClickListener (view1 -> clickButtonChooseImageMedicine());

        btnCreateNewMedicine.setOnClickListener (view1 -> clickButtonCreateNewMedicine());
    }

    //  handle image
    private void clickButtonChooseImageMedicine() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission (requireContext (), PERM) == PackageManager.PERMISSION_GRANTED)
                pickImageFromGallery ();
            else if (shouldShowRequestPermissionRationale(PERM)){
                showInContextUI ("Request Permission", "We need read storage permission to open gallery, please");
            }else
                requestPermissionLauncher.launch(PERM);
        }else
            pickImageFromGallery();
    }

    private void showInContextUI(String title, String message) {
        final Dialog dialog = new Dialog (getContext ());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.layout_dialog);

        Window window = dialog.getWindow ();
        if (window == null)
            return ;

        window.setLayout (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes ();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes (windowAttributes);
        dialog.setCancelable (false);

        TextView tvTitleDialog = dialog.findViewById (R.id.tvTitleDialog);
        TextView tvMessageDialog = dialog.findViewById (R.id.tvMessageDialog);
        AppCompatButton btnDisagree = dialog.findViewById (R.id.btnDisagree);
        AppCompatButton btnAgree = dialog.findViewById (R.id.btnAgree);

        tvTitleDialog.setText (title);
        tvMessageDialog.setText (message);
        btnDisagree.setText ("No");
        btnAgree.setText ("Yes");

        btnDisagree.setOnClickListener (view -> {
            dialog.dismiss ();
        });
        btnAgree.setOnClickListener (view -> {
            dialog.dismiss ();
            requestPermission();
        });
        dialog.show ();
    }

    private void requestPermission(){
        requestPermissionLauncher.launch(PERM);
    }

    private void pickImageFromGallery() {
        mGetContent.launch("image/*");
    }

//    upload image to firebase
    private void clickButtonCreateNewMedicine(){
        if (uriFilePath != null) {
            String img = uriFilePath.toString ();
            String name = etNameMedicine.getText ().toString ();
            String price = etPriceMedicine.getText ().toString ();
            String information = etInformationMedicine.getText ().toString ();

            InputStream iStream = null;
            byte[] inputData = new byte[2048];
            try {
                iStream = requireActivity ().getContentResolver().openInputStream(uriFilePath);
                inputData = getBytes(iStream);
                iStream.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            if (img.isEmpty ())
                Toast.makeText (requireContext (), "Please choose image for medicine!", Toast.LENGTH_SHORT).show ();
            else if (name.isEmpty ())
                Toast.makeText (requireContext (), "Name of your medicine is not valid!!", Toast.LENGTH_SHORT).show ();
            else if (price.isEmpty ())
                Toast.makeText (requireContext (), "Price of your medicine is not valid!!", Toast.LENGTH_SHORT).show ();
            else if (information.isEmpty ())
                Toast.makeText (requireContext (), "Information of your medicine is not valid!!", Toast.LENGTH_SHORT).show ();
            else
                price += " VND";

            Medicine medicine = new Medicine (img, name, price, information, inputData);

            addNewMedicineToRoom (medicine);
            Toast.makeText (requireContext (), "Successfully Created Medicine", Toast.LENGTH_SHORT).show ();
            FragmentTransaction transaction = requireActivity ().getSupportFragmentManager ().beginTransaction ();
            transaction.replace (R.id.fragment_content, new HomeFragment ());
            transaction.commit ();
            NavActivity.FRAGMENT_CURRENT = NavActivity.FRAGMENT_HOME;
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void addNewMedicineToRoom(Medicine medicine){
        ProductFragment.medicineDAO.insertMedicine (medicine);
    }
}