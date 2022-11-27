package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybanthuocdongy.R;

import java.io.File;
import java.util.List;

import model.Medicine;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    public interface MedicineItemClickListener{
        void onClickMedicine(Medicine medicine, int position);
    }

    private final List<Medicine> medicineList;
    private final Context context;
    private MedicineItemClickListener itemClickListener;

    public MedicineAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from (context);
        View view =layoutInflater.inflate (R.layout.itemmedicine, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get (position);
        if (medicine == null)
            return;

        File imgFile = new  File(medicine.getImg ());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.img.setImageBitmap(myBitmap);
        }
        holder.tvName.setText (medicine.getName ());
        holder.tvPrice.setText (medicine.getPrice ());
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
        TextView tvName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            img = itemView.findViewById (R.id.img);
            tvName = itemView.findViewById (R.id.tvName);
            tvPrice = itemView.findViewById (R.id.tvPrice);

            itemView.setOnClickListener (view -> {
                if(itemClickListener != null)
                    itemClickListener.onClickMedicine (medicineList.get (getAbsoluteAdapterPosition ()), getAbsoluteAdapterPosition ());
            });
        }
    }
}
