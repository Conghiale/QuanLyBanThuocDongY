package com.example.quanlybanthuocdongy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlybanthuocdongy.databinding.ActivityInformationMedicineBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import model.Medicine;

public class ShowInfomationActivity extends AppCompatActivity {
    private ActivityInformationMedicineBinding informationMedicineBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        informationMedicineBinding = ActivityInformationMedicineBinding.inflate (getLayoutInflater ());
        setContentView (informationMedicineBinding.getRoot ());
        setTitle ("Show Information Medicine");

        loadInformation();
    }

    @SuppressLint("SetTextI18n")
    private void loadInformation() {
        Intent intent = getIntent ();
        Medicine medicine = intent.getParcelableExtra ("Medicine");

//        load image
        File imgFile = new  File(medicine.getImg ());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            informationMedicineBinding.img.setImageBitmap(myBitmap);
        }

//        load Name
        informationMedicineBinding.tvNameMedicine.setText (medicine.getName ());
//        load ID
        informationMedicineBinding.tvIDMedicine.setText ("ID: " + medicine.getId ());
//        load Price
        informationMedicineBinding.tvPriceMedicine.setText (medicine.getPrice ());
//        load Information
        String nameMedicine = medicine.getName ();

//        try {
//            FileInputStream fileInputStream = openFileInput("information_medicines.txt");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (fileInputStream));
//            String line = bufferedReader.readLine();
//            while (line != null){
//                String[] strMedicine = line.split ("_");
//                if(medicine.getName ().equals (strMedicine[1])){
//                    String inf = strMedicine[3];
//                    Log.e ("TAG", "loadInformation: " + inf);
//                    informationMedicineBinding.tvInfMedicine.setText (inf);
//                    break;
//                }else{
//                    line = bufferedReader.readLine ();
//                }
//            }
//            fileInputStream.close();
//            bufferedReader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File file = new File (getFilesDir (), "information_medicines.txt");
        try {
            Scanner readFile = new Scanner (file);
            while (readFile.hasNextLine ()){
                String[] strMedicine = readFile.nextLine ().split ("_");
                if(medicine.getName ().equals (strMedicine[1])){
                    String infMedicine = strMedicine[3].replace ("/n", "\n");
                    informationMedicineBinding.tvInfMedicine.setText (infMedicine);
                    break;
                }
            }
            readFile.close ();
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
    }
}
