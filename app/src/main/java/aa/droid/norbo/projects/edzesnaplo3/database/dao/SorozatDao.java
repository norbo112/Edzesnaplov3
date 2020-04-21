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

    @Transaction
    @Query("SELECT * FROM sorozattabla WHERE naplodatum =:naplodatum")
    List<SorozatWithGyakorlat> getSorozatWithGyakorlatToList(String naplodatum);

    @Transaction
    @Query(
            "SELECT SUM(suly * ismetles) FROM sorozattabla WHERE naplodatum = :naplodatum"
    )
    int getOsszSulyByNaplo(String naplodatum);

    @Transaction
    @Query(
            "SELECT sum(suly * ismetles) FROM sorozattabla WHERE gyakorlatid = :gyakorlatid group by naplodatum order by naplodatum desc limit 1"
    )
    int getSorozatKorabbiOsszsuly(int gyakorlatid);
}
