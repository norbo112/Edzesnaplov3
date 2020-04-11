package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

@Dao
public interface NaploDao {
    @Insert
    void insertNaplo(Naplo naplo);

    @Query("SELECT * FROM naplo")
    LiveData<List<Naplo>> getNaploLiveData();

    @Query("DELETE FROM naplo")
    void deleteAll();

    @Delete
    void deleteNaplo(Naplo naplo);

    @Update
    void updateNaplo(Naplo naplo);
}
