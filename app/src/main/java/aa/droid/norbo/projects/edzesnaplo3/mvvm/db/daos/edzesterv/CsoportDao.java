package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.room.Dao;
import androidx.room.Query;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.CsoportEntity;

@Dao
public interface CsoportDao extends BaseDao<CsoportEntity> {
    @Query("DELETE FROM csoportentity WHERE edzesTervId =:tervId")
    void deleteById(int tervId);
}
