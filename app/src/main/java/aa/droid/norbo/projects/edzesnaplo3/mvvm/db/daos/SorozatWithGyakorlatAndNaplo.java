package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos;

import androidx.room.Embedded;
import androidx.room.Relation;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Sorozat;

public class SorozatWithGyakorlatAndNaplo {
    @Embedded public Sorozat sorozat;
    @Relation(
            parentColumn = "gyakorlatid",
            entityColumn = "id"
    )
    public Gyakorlat gyakorlat;

    @Relation(
            parentColumn = "naplodatum",
            entityColumn = "naplodatum"
    )
    public Naplo naplo;
}
