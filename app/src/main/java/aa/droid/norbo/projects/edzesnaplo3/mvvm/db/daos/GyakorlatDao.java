package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;

@Dao
public interface GyakorlatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Gyakorlat gyakorlat);

    @Query("SELECT * FROM gyakorlattabla")
    LiveData<List<Gyakorlat>> getGyakorlatLiveData();

    @Delete
    void deleteGyakorlat(Gyakorlat gyakorlat);

    @Update
    void updateGyakorlat(Gyakorlat gyakorlat);

    @Query("DELETE FROM gyakorlattabla")
    void deleteAll();

    @Query("SELECT count(*) FROM gyakorlattabla")
    int countRows();

    @Query("SELECT * FROM gyakorlattabla WHERE csoport IN (:izomcsoportok)")
    LiveData<List<Gyakorlat>> getByIzomcsoport(List<String> izomcsoportok);

    @Query("SELECT * FROM gyakorlattabla ORDER BY megnevezes")
    List<Gyakorlat> getGyakorlatList();
}
