package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.ui.ProductFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import model.Medicine;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    public interface MedicineItemClickListener{
        void onClickMedicine(Medicine medicine, ImageView image);
        void onClickAddToCart(AppCompatButton btnAddToCart, Medicine medicine);
    }

    private List<Medicine> medicineList;
    private Context context;
    private MedicineItemClickListener itemClickListener;

    public MedicineAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredMedicineList(List<Medicine> filteredMedicineList){
        this.medicineList = filteredMedicineList;
        notifyDataSetChanged ();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMedicineList(List<Medicine> medicineList){
        this.medicineList = medicineList;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from (parent.getContext ());
        View view =layoutInflater.inflate (R.layout.itemmedicine, parent, false);
        context = parent.getContext ();
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get (position);
        if (medicine == null)
            return;

//        ProductFragment.loadImage (context, holder.img, medicine.getImg ());
        holder.img.setImageBitmap (loadImage (medicine));
        holder.tvName.setText (medicine.getName ());
        holder.tvPrice.setText (medicine.getPrice ());
        holder.tvID.setText (String.valueOf (medicine.getId ()));

        if (medicine.isAddToCart ())
            holder.btnAddToCart.setBackgroundResource (R.drawable.bg_green_animation_end);
        else
            holder.btnAddToCart.setBackgroundResource (R.drawable.custom_button_green);

        holder.btnAddToCart.setOnClickListener (view -> {
            if (itemClickListener != null && !medicine.isAddToCart ()){
                itemClickListener.onClickAddToCart (holder.btnAddToCart, medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (medicineList.size () > 0)
            return medicineList.size ();
        return 0;
    }

    public void setItemClickListener(MedicineItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvName, tvPrice, tvID;
        AppCompatButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            img = itemView.findViewById (R.id.img);
            tvName = itemView.findViewById (R.id.tvName);
            tvPrice = itemView.findViewById (R.id.tvPrice);
            tvID = itemView.findViewById (R.id.tvID);
            btnAddToCart = itemView.findViewById (R.id.btnAddToCart);

            itemView.setOnClickListener (view -> {
                if(itemClickListener != null)
                    itemClickListener.onClickMedicine (medicineList.get (getAbsoluteAdapterPosition ()), img);
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
