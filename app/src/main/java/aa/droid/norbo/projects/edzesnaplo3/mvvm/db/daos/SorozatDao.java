package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;


@Dao
public interface SorozatDao {
    @Insert
    void insert(Sorozat sorozat);

    @Insert
    void insert(List<Sorozat> sorozats);

    @Transaction
    @Query("SELECT * FROM sorozattabla")
    LiveData<List<SorozatWithGyakorlatAndNaplo>> getAllWithNaplo();

    @Query("SELECT * FROM sorozattabla")
    LiveData<List<Sorozat>> getall();

    @Query("SELECT * FROM sorozattabla WHERE gyakorlatid = :gyakid ORDER BY naplodatum DESC")
    LiveData<List<Sorozat>> getallByGyakorlat(int gyakid);

    @Query("DELETE FROM sorozattabla WHERE naplodatum = :naplodatum")
    void delete(String naplodatum);

    @Query("DELETE FROM sorozattabla")
    void deleteAll();

    @Query(
            "SELECT gyakorlattabla.csoport AS csoport, gyakorlattabla.megnevezes AS gynev, " +
                    "SUM(sorozattabla.suly * sorozattabla.ismetles) AS osszsuly FROM " +
                    "sorozattabla " +
                    "INNER JOIN gyakorlattabla ON sorozattabla.gyakorlatid = gyakorlattabla.id " +
                    "WHERE sorozattabla.naplodatum = :naplodatum " +
                    "GROUP BY gynev"
    )
    Cursor selectAll(String naplodatum);

    @Transaction
    @Query("SELECT * FROM sorozattabla")
    LiveData<List<SorozatWithGyakorlat>> getAllSorozat();

    @Transaction
    @Query("SELECT * FROM sorozattabla WHERE naplodatum =:naplodatum")
    LiveData<List<SorozatWithGyakorlat>> getSorozatWithGyakorlat(long naplodatum);

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
            "SELECT sum(suly * ismetles) FROM sorozattabla WHERE gyakorlatid = :gyakorlatid group by naplodatum order by naplodatum DESC limit 1"
    )
    int getSorozatKorabbiOsszsuly(int gyakorlatid);
}
