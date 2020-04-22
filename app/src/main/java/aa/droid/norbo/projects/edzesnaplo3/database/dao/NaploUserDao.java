package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;

@Dao
public interface NaploUserDao {
    @Query("DELETE FROM naplouser")
    void deleteAll();

    @Query("SELECT * FROM naplouser LIMIT 1")
    LiveData<NaploUser> getNaploUser();

    @Insert
    void insert(NaploUser naploUser);

    @Transaction
    @Query("SELECT count(*) FROM naplouser")
    int count();
}
