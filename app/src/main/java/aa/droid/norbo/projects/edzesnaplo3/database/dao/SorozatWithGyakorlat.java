package aa.droid.norbo.projects.edzesnaplo3.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
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

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return gyakorlat.getMegnevezes()+" "+sorozat;
    }
}
