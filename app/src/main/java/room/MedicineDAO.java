package room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Medicine;

@Dao
public interface MedicineDAO {
//    get
    @Query ("SELECT * FROM medicines")
    List<Medicine> getAllMedicine();

    @Query ("SELECT * FROM medicines ORDER BY id DESC")
    LiveData<List<Medicine>> getAllMedicineLive();

    @Query ("SELECT * FROM medicines WHERE id = :id")
    Medicine getMedicineByID(int id);

//    insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicines(Medicine... medicines);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicine(Medicine medicine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicineList(List<Medicine> medicineList);

//    update
    @Update
    void updateMedicines(Medicine... medicines);

    @Update
    void updateMedicine(Medicine medicine);

//    delete
    @Delete
    void deleteMedicines(Medicine... medicines);

    @Delete
    void deleteStudent(Medicine medicine);

    @Query("DELETE FROM medicines")
    void deleteAllMedicine();

    @Query ("DELETE FROM medicines WHERE id = :id")
    void deleteMedicineByID(int id);

    @Query ("DELETE FROM medicines WHERE id in (:id)")
    void deleteMedicinesByIDs(int... id);
}
