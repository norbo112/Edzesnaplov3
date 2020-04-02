package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

@Dao
public interface SorozatDao {
    @Insert
    void insert(Sorozat sorozat);

    @Insert
    void insert(List<Sorozat> sorozats);

    @Query("DELETE FROM sorozattabla WHERE naplodatum = :naplodatum")
    void delete(String naplodatum);

    @Query("DELETE FROM sorozattabla")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM sorozattabla")
    LiveData<List<SorozatWithGyakorlat>> getAllSorozat();

    @Query("SELECT * FROM sorozattabla WHERE naplodatum = :mentettdatum")
    LiveData<List<Sorozat>> getSorozatByNaplo(String mentettdatum);

    @Transaction
    @Query("SELECT * FROM sorozattabla WHERE naplodatum =:naplodatum")
    LiveData<List<SorozatWithGyakorlat>> getSorozatWithGyakorlat(String naplodatum);
}
