package room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import model.Medicine;

@Database (entities = {Medicine.class}, version = 1)
public abstract class MedicineDB extends RoomDatabase {
    private static MedicineDB INSTANCE;

    public synchronized static MedicineDB getInstance(Context context){
        if(INSTANCE == null)
            INSTANCE = builDatabase(context);
        return INSTANCE;
    }

    private static MedicineDB builDatabase(Context context) {
        return Room.databaseBuilder (context, MedicineDB.class, "medicines.db")
                .addCallback (new Callback () {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate (db);
                        Executors.newSingleThreadScheduledExecutor ().execute (() -> {
                            getInstance (context).medicineDAO ().insertMedicineList (Medicine.sampleData ());
                        });
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen (db);
                        Executors.newSingleThreadScheduledExecutor ().execute (() -> {
                            Log.e ("TAG", "databaseBuilder - onOpen." + getInstance (context).medicineDAO ().getAllMedicine ().size ());
                            if (getInstance (context).medicineDAO ().getAllMedicine ().isEmpty ()){
                                getInstance (context).medicineDAO ().insertMedicineList (Medicine.sampleData ());
                            }
                        });
                    }
                }).fallbackToDestructiveMigration().allowMainThreadQueries ().build ();
    }

    public abstract MedicineDAO medicineDAO();
}
