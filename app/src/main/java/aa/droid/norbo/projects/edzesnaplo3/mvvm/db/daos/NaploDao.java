package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

@Dao
public interface NaploDao {
    @Insert
    void insertNaplo(Naplo naplo);

    @Transaction
    @Query("SELECT * FROM naplo")
    LiveData<List<NaploWithSorozat>> getNaploWithSorozats();

    @Query("SELECT * FROM naplo ORDER BY naplodatum ASC")
    LiveData<List<Naplo>> getAll();

    @Query("SELECT * FROM naplo ORDER BY naplodatum")
    Cursor selectAll();

    @Query("DELETE FROM naplo")
    void deleteAll();

    @Delete
    void deleteNaplo(Naplo naplo);

    @Update
    void updateNaplo(Naplo naplo);
}
