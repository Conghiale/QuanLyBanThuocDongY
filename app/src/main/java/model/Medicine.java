package model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
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
    private String infMedicine;
    private boolean isAddToCart;
    private int quantityChooseBuy;
    private double total;
    private byte[] byteImage;

    public Medicine() {
    }

    public Medicine(String img, String name, String price) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.isAddToCart = false;
        this.quantityChooseBuy= 1;
        this.total = getTotal ();
        this.infMedicine = "";
        this.byteImage = new byte[2048];
    }

    public Medicine(String img, String name, String price, String infMedicine, boolean isAddToCart) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.isAddToCart = isAddToCart;
        this.quantityChooseBuy= 1;
        this.total = getTotal ();
        this.infMedicine = infMedicine;
        this.byteImage = new byte[2048];
    }

    public Medicine(String img, String name, String price, boolean isAddToCart) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.isAddToCart = isAddToCart;
        this.quantityChooseBuy= 1;
        this.total = getTotal ();
        this.infMedicine = "";
        this.byteImage = new byte[2048];
    }

    public Medicine(String img, String name, String price, String infMedicine, byte[] byteImage) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.isAddToCart = false;
        this.quantityChooseBuy= 1;
        this.total = getTotal ();
        this.infMedicine = infMedicine;
        this.byteImage = byteImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Medicine(Parcel in) {
        id = in.readInt ();
        img = in.readString ();
        name = in.readString ();
        price = in.readString ();
        isAddToCart = in.readByte () != 0;
        quantityChooseBuy = in.readInt ();
        total = in.readDouble ();
        infMedicine = in.readString ();
        byteImage = in.createByteArray ();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine> () {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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

    public boolean isAddToCart() {
        return isAddToCart;
    }

    public void setAddToCart(boolean addToCart) {
        isAddToCart = addToCart;
    }

    public int getQuantityChooseBuy() {
        return quantityChooseBuy;
    }

    public void setQuantityChooseBuy(int quantityChooseBuy) {
        this.quantityChooseBuy = quantityChooseBuy;
    }

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    public String getInfMedicine() {
        return infMedicine;
    }

    public void setInfMedicine(String infMedicine) {
        this.infMedicine = infMedicine;
    }

    public double getTotal() {
        String[] arrPrice = this.price.split (" ");
        double cost = Double.parseDouble (arrPrice[0]);
        return cost * this.quantityChooseBuy;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", infMedicine='" + infMedicine + '\'' +
                ", isAddToCart=" + isAddToCart +
                ", quantityChooseBuy=" + quantityChooseBuy +
                ", total=" + total +
                '}';
    }

    public static List<Medicine> sampleData() {
        List<Medicine> medicineList = new ArrayList<> ();

        Medicine m1 = new Medicine ("image/i0.jpg", "Boganic (Hộp 5 vỉ x 20 viên)", "72.000 VND/Hộp");
        Medicine m2 = new Medicine ("image/i1.jpg", "Bổ phế Nam Hà chỉ khái lộ (Chai 125ml)", "38.000 VND/Hộp");
        Medicine m3 = new Medicine ("image/i2.jpg", "Viên ngậm Bổ Phế Nam Hà XC", "22.000 VND/Hộp");
        Medicine m4 = new Medicine ("image/i3.jpg", "Cao Ích Mẫu (180ml)", "39.000 VND/Chai");
        Medicine m5 = new Medicine ("image/i4.jpg", "Didicera - Traphaco (Hộp 10 gói x 5g hoàn cứng)", "40.000 VND/Hộp");
        Medicine m6 = new Medicine ("image/i5.jpg", "Viên Hà Thủ Ô EXTRACAP® (Hộp 5 vỉ x 10 viên nang cứng)", "63.000 VND/Hộp");
        Medicine m7 = new Medicine ("image/i6.jpg", "GARLICAP® Viên Tỏi Nghệ (Hộp 5 vỉ x 10 viên nang cứng)", "30.000 VND/Hộp");
        Medicine m8 = new Medicine ("image/i7.jpg", "Hà Thủ Ô (Hộp 5 vỉ x 20 viên)", "45.000 VND/Hộp");
        Medicine m9 = new Medicine ("image/i8.jpg", "Hoạt Huyết Dưỡng Não (Hộp 5 vỉ x 20 viên)", "95.000 VND/Hộp");
        Medicine m10 = new Medicine ("image/i9.jpg", "Hoạt huyết Nhất Nhất (Hộp 3 vỉ x 10 viên)", "110.000 VND/Hộp");
        Medicine m11 = new Medicine ("image/i10.jpg", "Phong Thấp Nam Hà (Hộp 1 lọ x 60 viên nang cứng)", "56.000 VND/Hộp");
        Medicine m12 = new Medicine ("image/i11.jpg", "MIMOSA® Viên an thần (Hộp 5 vỉ x 10 viên)", "65.000 VND/Hộp");
        Medicine m13 = new Medicine ("image/i12.jpg", "Stilux 60mg", "93.000 VND/Hộp");
        Medicine m14 = new Medicine ("image/i13.jpg", "Tam Thất OPC (Hộp 2 vỉ x 10 viên nang mềm)", "135.000 VND/Hộp");
        Medicine m15 = new Medicine ("image/i14.jpg", "Thông Xoang Tán Nam Dược (Hộp 1 lọ x 50 viên)", "102.000 VND/Hộp");
        Medicine m16 = new Medicine ("image/i15.jpg", "BAR – Thuốc Lợi Gan Mật (Hộp 1 lọ x 180 viên nén bao đường)", "56.000 VND/Hộp");
        Medicine m17 = new Medicine ("image/i16.jpg", "Tonka Nhất Nhất (Hộp 1 lọ x 60 viên nền bao phim)", "85.000 VND/Hộp");
        Medicine m18 = new Medicine ("image/i17.jpg", "Tottim Extra (Hộp 2 vỉ x 20 viên)", "83.000 VND/Hộp");
        Medicine m19 = new Medicine ("image/i18.jpg", "Tradin extra Traphaco (Hộp 2 vỉ x 10 viên)", "30.000 VND/Hộp");
        Medicine m20 = new Medicine ("image/i19.jpg", "VG-5 (Hộp 1 lọ x 40 viên bao phim)", "56.000 VND/Hộp");

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

        Log.e ("TAG", "sampleData: " + medicineList.size ());
        return medicineList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt (id);
        parcel.writeString (img);
        parcel.writeString (name);
        parcel.writeString (price);
        parcel.writeByte ((byte) (isAddToCart ? 1 : 0));
        parcel.writeInt (quantityChooseBuy);
        parcel.writeDouble (total);
        parcel.writeString (infMedicine);
        parcel.writeByteArray (byteImage);
    }
}
