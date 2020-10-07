package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;

public class NaploWithOnlySorozats {
    @Embedded public Naplo naplo;
    @Relation(
            parentColumn = "naplodatum",
            entityColumn = "naplodatum",
            entity = Sorozat.class
    )
    public List<Sorozat> sorozats;

    public NaploWithOnlySorozats() {
    }

    @Ignore
    public NaploWithOnlySorozats(Naplo naplo, List<Sorozat> sorozats) {
        this.naplo = naplo;
        this.sorozats = sorozats;
    }
}
