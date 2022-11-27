package model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "medicines")
public class Medicine implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String img;
    private String name;
    private String price;

    public Medicine() {
    }

    public Medicine(String img, String name, String price) {
        this.img = img;
        this.name = name;
        this.price = price;
    }

    protected Medicine(Parcel in) {
        id = in.readInt ();
        img = in.readString ();
        name = in.readString ();
        price = in.readString ();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine> () {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine (in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public static List<Medicine> sampleData() {
        List<Medicine> medicineList = new ArrayList<> ();

        @SuppressLint("SdCardPath") Medicine m1 = new Medicine ("/sdcard/Pictures/image/i1.jpg", "Boganic (Hộp 5 vỉ x 20 viên)", "72.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m2 = new Medicine ("/sdcard/Pictures/image/i2.jpg", "Bổ phế Nam Hà chỉ khái lộ (Chai 125ml)", "38.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m3 = new Medicine ("/sdcard/Pictures/image/i3.jpg", "Viên ngậm Bổ Phế Nam Hà XC", "22.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m4 = new Medicine ("/sdcard/Pictures/image/i4.jpg", "Cao Ích Mẫu (180ml)", "39.000 VND/Chai");
        @SuppressLint("SdCardPath") Medicine m5 = new Medicine ("/sdcard/Pictures/image/i5.jpg", "Didicera - Traphaco (Hộp 10 gói x 5g hoàn cứng)", "40.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m6 = new Medicine ("/sdcard/Pictures/image/i6.jpg", "Viên Hà Thủ Ô EXTRACAP® (Hộp 5 vỉ x 10 viên nang cứng)", "63.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m7 = new Medicine ("/sdcard/Pictures/image/i7.jpg", "GARLICAP® Viên Tỏi Nghệ (Hộp 5 vỉ x 10 viên nang cứng)", "30.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m8 = new Medicine ("/sdcard/Pictures/image/i8.jpg", "Hà Thủ Ô (Hộp 5 vỉ x 20 viên)", "45.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m9 = new Medicine ("/sdcard/Pictures/image/i9.jpg", "Hoạt Huyết Dưỡng Não (Hộp 5 vỉ x 20 viên)", "95.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m10 = new Medicine ("/sdcard/Pictures/image/i10.jpg", "Hoạt huyết Nhất Nhất (Hộp 3 vỉ x 10 viên)", "110.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m11 = new Medicine ("/sdcard/Pictures/image/i11.jpg", "Phong Thấp Nam Hà (Hộp 1 lọ x 60 viên nang cứng)", "56.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m12 = new Medicine ("/sdcard/Pictures/image/i12.jpg", "MIMOSA® Viên an thần (Hộp 5 vỉ x 10 viên)", "65.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m13 = new Medicine ("/sdcard/Pictures/image/i13.jpg", "Stilux 60mg", "93.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m14 = new Medicine ("/sdcard/Pictures/image/i14.jpg", "Tam Thất OPC (Hộp 2 vỉ x 10 viên nang mềm)", "135.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m15 = new Medicine ("/sdcard/Pictures/image/i15.jpg", "Thông Xoang Tán Nam Dược (Hộp 1 lọ x 50 viên)", "102.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m16 = new Medicine ("/sdcard/Pictures/image/i16.jpg", "BAR – Thuốc Lợi Gan Mật (Hộp 1 lọ x 180 viên nén bao đường)", "56.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m17 = new Medicine ("/sdcard/Pictures/image/i17.jpg", "Tonka Nhất Nhất (Hộp 1 lọ x 60 viên nền bao phim)", "85.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m18 = new Medicine ("/sdcard/Pictures/image/i18.jpg", "Tottim Extra (Hộp 2 vỉ x 20 viên)", "83.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m19 = new Medicine ("/sdcard/Pictures/image/i19.jpg", "Tradin extra Traphaco (Hộp 2 vỉ x 10 viên)", "30.000 VND/Hộp");
        @SuppressLint("SdCardPath") Medicine m20 = new Medicine ("/sdcard/Pictures/image/i20.jpg", "VG-5 (Hộp 1 lọ x 40 viên bao phim)", "56.000 VND/Hộp");

        medicineList.add(m1);
        medicineList.add(m2);
        medicineList.add(m3);
        medicineList.add(m4);
        medicineList.add(m5);
        medicineList.add(m6);
        medicineList.add(m7);
        medicineList.add(m8);
        medicineList.add(m9);
        medicineList.add(m10);
        medicineList.add(m11);
        medicineList.add(m12);
        medicineList.add(m13);
        medicineList.add(m14);
        medicineList.add(m15);
        medicineList.add(m16);
        medicineList.add(m17);
        medicineList.add(m18);
        medicineList.add(m19);
        medicineList.add(m20);

        Log.e ("TAG", "sampleData: ");
        return medicineList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt (id);
        parcel.writeString (img);
        parcel.writeString (name);
        parcel.writeString (price);
    }
}
