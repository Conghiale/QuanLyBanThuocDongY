package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.DownloadImageTask;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.ui.ProductFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import model.Medicine;

public class MedicineCartAdapter extends RecyclerView.Adapter<MedicineCartAdapter.ViewHolder> {
    public interface MedicineCartItemClickListener {
        void onClickMedicine(Medicine medicine, int position);
        void onClickRemoveToCart(Medicine medicine);
        void onClickNegativeCount(Medicine medicine);
        void onClickPositionCount(Medicine medicine);
    }

    private List<Medicine> medicineCartList;
    private final Context context;
    private MedicineCartItemClickListener itemClickListener;
    private Uri uri;

    public MedicineCartAdapter(List<Medicine> medicineCartList, Context context) {
        this.medicineCartList = medicineCartList;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMedicineCartList(List<Medicine> MedicineCartList){
        this.medicineCartList = MedicineCartList;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from (context);
        View view =layoutInflater.inflate (R.layout.item_medicine_cart, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineCartList.get (position);
        if (medicine == null)
            return;

        holder.img.setImageBitmap (loadImage (medicine));
        holder.tvName.setText (medicine.getName ());
        holder.tvPrice.setText (medicine.getPrice ());
        holder.tvID.setText ("ID: " + String.valueOf (medicine.getId ()));

        holder.btnRemoveMedicineCart.setOnClickListener (view -> {
            if (itemClickListener != null){
                itemClickListener.onClickRemoveToCart ( medicine);
            }
        });

        holder.btnNegativeCount.setOnClickListener (view -> {

            if (medicine.getQuantityChooseBuy () > 1)
                medicine.setQuantityChooseBuy (medicine.getQuantityChooseBuy () - 1);
            else
                Toast.makeText (view.getContext (), "Minimum quantity to buy is 1", Toast.LENGTH_SHORT).show ();
            holder.tvCountMedicine.setText (String.valueOf (medicine.getQuantityChooseBuy ()));
            if (itemClickListener != null){
                itemClickListener.onClickNegativeCount ( medicine); // chua lam
            }
        });

        holder.tvCountMedicine.setText (String.valueOf (medicine.getQuantityChooseBuy ()));

        holder.btnPositionCount.setOnClickListener (view -> {
            medicine.setQuantityChooseBuy (medicine.getQuantityChooseBuy () + 1);
            holder.tvCountMedicine.setText (String.valueOf (medicine.getQuantityChooseBuy ()));
            if (itemClickListener != null){
                itemClickListener.onClickPositionCount ( medicine); // chua lam
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineCartList == null ? 0 : medicineCartList.size();
    }

    public void setItemClickListener(MedicineCartItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvName, tvPrice, tvID, tvCountMedicine;
        AppCompatButton btnRemoveMedicineCart, btnNegativeCount, btnPositionCount;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            img = itemView.findViewById (R.id.img);
            tvName = itemView.findViewById (R.id.tvName);
            tvPrice = itemView.findViewById (R.id.tvPrice);
            tvID = itemView.findViewById (R.id.tvID);
            tvCountMedicine = itemView.findViewById (R.id.tvCountMedicine);
            btnRemoveMedicineCart = itemView.findViewById (R.id.btnRemoveMedicineCart);
            btnNegativeCount = itemView.findViewById (R.id.btnNegativeCount);
            btnPositionCount = itemView.findViewById (R.id.btnPositionCount);

            itemView.setOnClickListener (view -> {
                if(itemClickListener != null)
                    itemClickListener.onClickMedicine (medicineCartList.get (getAbsoluteAdapterPosition ()), getAbsoluteAdapterPosition ());
            });
        }
    }

    public Bitmap loadImage(Medicine medicine){
        try (InputStream stream = context.getAssets ().open (medicine.getImg ())) {
            Bitmap bitmap = BitmapFactory.decodeStream (stream);
            stream.close ();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace ();
            return BitmapFactory.decodeByteArray(medicine.getByteImage (), 0, medicine.getByteImage ().length);
        }
    }
}

