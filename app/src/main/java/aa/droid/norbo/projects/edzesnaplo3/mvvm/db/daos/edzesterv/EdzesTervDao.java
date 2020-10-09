package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithGyakorlatTervek;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;

@Dao
public interface EdzesTervDao extends BaseDao<EdzesTervEntity> {
    @Transaction
    @Query("SELECT * FROM edzesterventity")
    LiveData<List<EdzesTervWithEdzesnap>> getAll();

    @Transaction
    @Query("SELECT * FROM edzesterventity")
    List<EdzesTervWithEdzesnap> getAllForTest();

    @Transaction
    @Query("SELECT * FROM edzesterventity")
    List<EdzesTervWithGyakorlatTervek> getAllGyakorlatForEdzestervToTest();
}
