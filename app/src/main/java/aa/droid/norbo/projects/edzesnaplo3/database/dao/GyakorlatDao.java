package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

@Dao
public interface GyakorlatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
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
}
