package aa.droid.norbo.projects.edzesnaplo3.database.dao.tapanyag;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;

@Dao
public interface ElelmiszerDao {
    @Insert
    void insert(Elelmiszer elelmiszer);

    @Insert
    void insert(List<Elelmiszer> elelmiszers);

    @Update
    void update(Elelmiszer elelmiszer);

    @Delete
    void delete(Elelmiszer elelmiszer);

    @Query("DELETE FROM tapanyagtabla")
    void deleteAll();

    @Query("SELECT * FROM tapanyagtabla")
    LiveData<List<Elelmiszer>> getLiveData();

    @Query("SELECT fajta FROM tapanyagtabla GROUP BY fajta ORDER BY fajta")
    LiveData<List<String>> getElelmiszerTipus();

    @Query("SELECT count(*) FROM tapanyagtabla")
    int countRows();
}
