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

        Medicine m1 = new Medicine ("image/i0.jpg", "Boganic (H???p 5 v??? x 20 vi??n)", "72.000 VND/H???p");
        Medicine m2 = new Medicine ("image/i1.jpg", "B??? ph??? Nam H?? ch??? kh??i l??? (Chai 125ml)", "38.000 VND/H???p");
        Medicine m3 = new Medicine ("image/i2.jpg", "Vi??n ng???m B??? Ph??? Nam H?? XC", "22.000 VND/H???p");
        Medicine m4 = new Medicine ("image/i3.jpg", "Cao ??ch M???u (180ml)", "39.000 VND/Chai");
        Medicine m5 = new Medicine ("image/i4.jpg", "Didicera - Traphaco (H???p 10 g??i x 5g ho??n c???ng)", "40.000 VND/H???p");
        Medicine m6 = new Medicine ("image/i5.jpg", "Vi??n H?? Th??? ?? EXTRACAP?? (H???p 5 v??? x 10 vi??n nang c???ng)", "63.000 VND/H???p");
        Medicine m7 = new Medicine ("image/i6.jpg", "GARLICAP?? Vi??n To??i Ngh???? (H???p 5 v??? x 10 vi??n nang c???ng)", "30.000 VND/H???p");
        Medicine m8 = new Medicine ("image/i7.jpg", "H?? Th??? ?? (H???p 5 v??? x 20 vi??n)", "45.000 VND/H???p");
        Medicine m9 = new Medicine ("image/i8.jpg", "Ho???t Huy???t D?????ng N??o (H???p 5 v??? x 20 vi??n)", "95.000 VND/H???p");
        Medicine m10 = new Medicine ("image/i9.jpg", "Ho???t huy???t Nh???t Nh???t (H???p 3 v??? x 10 vi??n)", "110.000 VND/H???p");
        Medicine m11 = new Medicine ("image/i10.jpg", "Phong Th???p Nam H?? (H???p 1 l??? x 60 vi??n nang c???ng)", "56.000 VND/H???p");
        Medicine m12 = new Medicine ("image/i11.jpg", "MIMOSA?? Vi??n an th???n (H???p 5 v??? x 10 vi??n)", "65.000 VND/H???p");
        Medicine m13 = new Medicine ("image/i12.jpg", "Stilux 60mg", "93.000 VND/H???p");
        Medicine m14 = new Medicine ("image/i13.jpg", "Tam Th???t OPC (H???p 2 v??? x 10 vi??n nang m???m)", "135.000 VND/H???p");
        Medicine m15 = new Medicine ("image/i14.jpg", "Th??ng Xoang T??n Nam D?????c (H???p 1 l??? x 50 vi??n)", "102.000 VND/H???p");
        Medicine m16 = new Medicine ("image/i15.jpg", "BAR ??? Thu???c L???i Gan M???t (H???p 1 l??? x 180 vi??n n??n bao ???????ng)", "56.000 VND/H???p");
        Medicine m17 = new Medicine ("image/i16.jpg", "Tonka Nh???t Nh???t (H???p 1 l??? x 60 vi??n n???n bao phim)", "85.000 VND/H???p");
        Medicine m18 = new Medicine ("image/i17.jpg", "Tottim Extra (H???p 2 v??? x 20 vi??n)", "83.000 VND/H???p");
        Medicine m19 = new Medicine ("image/i18.jpg", "Tradin extra Traphaco (H???p 2 vi?? x 10 vi??n)", "30.000 VND/H???p");
        Medicine m20 = new Medicine ("image/i19.jpg", "VG-5 (H???p 1 l??? x 40 vi??n bao phim)", "56.000 VND/H???p");

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
