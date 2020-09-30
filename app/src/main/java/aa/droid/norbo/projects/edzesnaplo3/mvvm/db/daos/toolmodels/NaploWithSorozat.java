package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;

public class NaploWithSorozat {
    @Embedded public Naplo daonaplo;
    @Relation(
            parentColumn = "naplodatum",
            entityColumn = "naplodatum",
            entity = Sorozat.class
    )
    public List<SorozatWithGyakorlat> sorozats;

    public NaploWithSorozat(){}

    @Ignore
    public NaploWithSorozat(Naplo naplo, List<SorozatWithGyakorlat> sorozats1) {
        this.daonaplo = naplo;
        this.sorozats = sorozats1;
    }
}
