package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

@Dao
public interface SorozatDao {
    @Insert
    void insert(Sorozat sorozat);

    @Insert
    void insert(Sorozat... sorozats);

    @Query("SELECT * FROM sorozattabla WHERE naploid = :mentettdatum")
    LiveData<List<Sorozat>> getSorozatByNaplo(String mentettdatum);
}
