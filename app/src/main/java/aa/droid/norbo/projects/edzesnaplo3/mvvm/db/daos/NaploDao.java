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

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

@Dao
public interface NaploDao {
    @Insert
    void insertNaplo(Naplo naplo);

    @Transaction
    @Query("SELECT * FROM naplo ORDER BY naplodatum DESC")
    LiveData<List<NaploWithSorozat>> getNaploWithSorozats();

    @Transaction
    @Query("SELECT * FROM naplo WHERE naplodatum = :naplodatum")
    LiveData<List<NaploWithSorozat>> getNaploWithSorozats(long naplodatum);

    @Transaction
    @Query("SELECT * FROM naplo WHERE naplodatum =:naplodatum")
    LiveData<NaploWithOnlySorozats> getNaploWithOnlySorozats(long naplodatum);

    @Query("SELECT * FROM naplo ORDER BY naplodatum ASC")
    LiveData<List<Naplo>> getAll();

    @Query("SELECT * FROM naplo ORDER BY naplodatum")
    Cursor selectAll();

    @Query("select sum(ismetles * suly) as ossz, naplodatum from sorozattabla group by naplodatum")
    Cursor selectOsszsulyByNaploDatum();

    @Query("DELETE FROM naplo")
    void deleteAll();

    @Delete
    void deleteNaplo(Naplo naplo);

    @Update
    void updateNaplo(Naplo naplo);

    @Query("DELETE FROM naplo WHERE naplodatum =:naplodatum")
    void delete(long naplodatum);

    @Transaction
    @Query("SELECT * FROM naplo WHERE naplodatum =:naplodatum")
    NaploWithOnlySorozats getSyncNaploWithOnlySorozats(long naplodatum);

    @Transaction
    @Query("SELECT * FROM naplo ORDER BY naplodatum DESC")
    List<NaploWithSorozat> getNaploWithSorozatList();

    @Query("SELECT * FROM naplo WHERE naplodatum =:naplodatum")
    Naplo getOneByDatum(long naplodatum);
}
