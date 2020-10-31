package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.room.Dao;
import androidx.room.Query;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;

@Dao
public interface GyakorlatTervDao extends BaseDao<GyakorlatTervEntity> {
    @Query("DELETE FROM gyakorlatterventity WHERE edzesTervId =:tervId")
    void deleteById(int tervId);
}
