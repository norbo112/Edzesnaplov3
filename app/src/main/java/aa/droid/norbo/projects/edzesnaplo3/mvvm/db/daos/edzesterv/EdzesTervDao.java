package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;

@Dao
public interface EdzesTervDao extends BaseDao<EdzesTervEntity> {
    @Query("SELECT * FROM edzesterventity")
    LiveData<EdzesTervWithEdzesnap> getAll();
}
