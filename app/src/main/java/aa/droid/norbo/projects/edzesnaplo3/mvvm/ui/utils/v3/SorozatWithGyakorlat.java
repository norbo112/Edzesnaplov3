package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

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
