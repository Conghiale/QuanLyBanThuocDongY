package model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MedicineViewModel extends ViewModel {
    private final MutableLiveData<List<Medicine>> medicineCartListLD;
    private final MutableLiveData<List<Integer>> listIDMedicineCartLD;
    private final MutableLiveData<List<Medicine>> medicineListLD;
    private final MutableLiveData<Medicine> medicineLD;
    private final MutableLiveData<Integer> countMedicineCartLD;

    public MedicineViewModel() {
        medicineCartListLD = new MutableLiveData<> ();
        listIDMedicineCartLD = new MutableLiveData<> ();
        medicineListLD = new MutableLiveData<> ();
        medicineLD = new MutableLiveData<> ();
        countMedicineCartLD = new MutableLiveData<> ();
        initObject();
    }

    private void initObject() {
        List<Medicine> medicineCartList = new ArrayList<> ();
        medicineCartListLD.postValue (medicineCartList);
    }

    public MutableLiveData<List<Medicine>> getMedicineCartListLD() {
        return medicineCartListLD;
    }

    public void setMedicineCartList(List<Medicine> medicineCartList){
        medicineCartListLD.postValue (medicineCartList);
    }

    public void setListIDMedicineCart(List<Integer> listIDMedicineCart){
        listIDMedicineCartLD.postValue (listIDMedicineCart);
    }

    public MutableLiveData<List<Integer>> getListIDMedicineCartLD() {
        return listIDMedicineCartLD;
    }

    public void setMedicineList(List<Medicine> medicineList){
        medicineListLD.postValue (medicineList);
    }

    public MutableLiveData<List<Medicine>> getMedicineListLD() {
        return medicineListLD;
    }

    public void setMedicine(Medicine medicine){
        medicineLD.postValue (medicine);
    }

    public MutableLiveData<Medicine> getMedicineLD() {
        return medicineLD;
    }

    public void setCountMedicineCart(Integer countMedicineCart){
        countMedicineCartLD.postValue (countMedicineCart);
    }

    public MutableLiveData<Integer> getCountMedicineCartLD() {
        return countMedicineCartLD;
    }
}
