package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

@Dao
public interface NaploDao {
    @Insert
    void insertNaplo(Naplo naplo);

    @Query("SELECT * FROM naplo")
    LiveData<Naplo> getNaploLiveData();

    @Delete
    void deleteNaplo(Naplo naplo);

    @Update
    void updateNaplo(Naplo naplo);
}
