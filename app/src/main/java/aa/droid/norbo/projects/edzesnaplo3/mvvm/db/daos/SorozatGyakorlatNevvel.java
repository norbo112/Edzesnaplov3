package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Sorozat;

public class SorozatGyakorlatNevvel {
    @Embedded public Sorozat sorozat;
    @Relation(
            parentColumn = "gyakorlatid",
            entityColumn = "id",
            entity = Gyakorlat.class,
            projection = {"megnevezes"}
    )
    public String gyakorlat;

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return gyakorlat+" "+sorozat;
    }
}
