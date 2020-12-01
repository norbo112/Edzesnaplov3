package aa.droid.norbo.projects.edzesnaplo3.mvvm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.NaploUserDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;


@Database(entities = {Gyakorlat.class, Sorozat.class, Naplo.class, NaploUser.class}, version = 1)
public abstract class EdzesNaploDatabase extends RoomDatabase {
    public static final String DBNAME = "edzesnaplov4_db";

    public abstract GyakorlatDao gyakorlatDao();
    public abstract NaploDao naploDao();
    public abstract SorozatDao sorozatDao();

}
