package aa.droid.norbo.projects.edzesnaplo3.mvvm.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;


@Database(entities = {Gyakorlat.class, Sorozat.class, Naplo.class, NaploUser.class}, version = 2)
public abstract class EdzesNaploDatabase extends RoomDatabase {
    public static final String DBNAME = "edzesnaplov4_db";

    public abstract GyakorlatDao gyakorlatDao();
    public abstract NaploDao naploDao();
    public abstract SorozatDao sorozatDao();

    public static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE sorozattabla ADD COLUMN szettek TEXT DEFAULT NULL");
        }
    };
}
