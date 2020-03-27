package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.room.Embedded;
import androidx.room.Relation;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class SorozatWithGyakorlat {
    @Embedded public Sorozat sorozat;
    @Relation(
            parentColumn = "gyakorlatid",
            entityColumn = "id"
    )
    public Gyakorlat gyakorlat;
}
