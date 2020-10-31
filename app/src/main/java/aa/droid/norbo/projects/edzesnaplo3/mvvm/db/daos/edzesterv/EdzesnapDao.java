package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.room.Dao;
import androidx.room.Query;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesnapEntity;

@Dao
public interface EdzesnapDao extends BaseDao<EdzesnapEntity> {
    @Query("DELETE FROM edzesnapentity WHERE edzestervId =:tervId")
    void deleteById(int tervId);
}
