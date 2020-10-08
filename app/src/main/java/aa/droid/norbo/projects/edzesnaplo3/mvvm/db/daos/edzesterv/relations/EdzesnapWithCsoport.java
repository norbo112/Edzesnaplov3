package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.CsoportEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesnapEntity;

public class EdzesnapWithCsoport {
    @Embedded public EdzesnapEntity edzesnapEntity;
    @Relation(
            entity = CsoportEntity.class,
            parentColumn = "id",
            entityColumn = "edzesNapId"
    )
    public List<CsoportWithGyakorlatTerv> csoportsWithGyakorlat;
}
