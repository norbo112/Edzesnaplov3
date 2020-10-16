package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;

public class EdzesTervWithGyakorlatTervek {
    @Embedded public EdzesTervEntity edzesTervEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "edzesTervId"
    )
    public List<GyakorlatTervEntity> gyakorlatTervEntity;
}
