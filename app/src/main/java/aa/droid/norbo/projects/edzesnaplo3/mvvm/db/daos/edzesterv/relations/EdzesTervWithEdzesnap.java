package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesnapEntity;

public class EdzesTervWithEdzesnap {
    @Embedded public EdzesTervEntity edzesTervEntity;
    @Relation(
            entity = EdzesnapEntity.class,
            parentColumn = "edzestervId",
            entityColumn = "id"
    )
    public List<EdzesnapWithCsoport> edzesnapList;
}
