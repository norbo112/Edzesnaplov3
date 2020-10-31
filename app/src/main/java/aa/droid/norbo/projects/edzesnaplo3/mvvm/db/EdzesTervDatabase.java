package aa.droid.norbo.projects.edzesnaplo3.mvvm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.converters.ListOfIntegers;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.NaploUserDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.CsoportDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesnapDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.GyakorlatTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.CsoportEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesnapEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;


@TypeConverters({ListOfIntegers.class})
@Database(entities = {EdzesTervEntity.class, GyakorlatTervEntity.class, EdzesnapEntity.class, CsoportEntity.class}, version = 1)
public abstract class EdzesTervDatabase extends RoomDatabase {
    private static final String TAG = "EdzesTervDatabase";
    public static final String DBNAME = "edzestervv4_db";

    public abstract EdzesnapDao edzesnapDao();
    public abstract CsoportDao csoportDao();
    public abstract GyakorlatTervDao gyakorlatTervDao();
    public abstract EdzesTervDao edzesTervDao();
}
