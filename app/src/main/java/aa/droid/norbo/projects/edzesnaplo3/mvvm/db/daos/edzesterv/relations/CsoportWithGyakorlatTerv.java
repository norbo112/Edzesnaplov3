package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.CsoportEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;

public class CsoportWithGyakorlatTerv {
    @Embedded public CsoportEntity csoportEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "csoportId"
    )
    public List<GyakorlatTervEntity> gyakorlatTervEntities;
}
