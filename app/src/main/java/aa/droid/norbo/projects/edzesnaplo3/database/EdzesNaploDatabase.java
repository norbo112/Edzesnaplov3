package aa.droid.norbo.projects.edzesnaplo3.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploUserDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.models.GyakorlatCsomag;

@Database(entities = {Gyakorlat.class, Sorozat.class, Naplo.class, NaploUser.class}, version = 1)
public abstract class EdzesNaploDatabase extends RoomDatabase {
    private static final String TAG = "EdzésnaplóAdatbázis";

    public abstract GyakorlatDao gyakorlatDao();
    public abstract NaploDao naploDao();
    public abstract SorozatDao sorozatDao();
    public abstract NaploUserDao naploUserDao();

    public static volatile EdzesNaploDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService dbWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EdzesNaploDatabase getDatabase(final Context context) {
        if(INSTANCE == null)  {
            synchronized (EdzesNaploDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EdzesNaploDatabase.class, "edzesnaplo_db")
                            .addCallback(new AdatFeltoltes(context))
                            .allowMainThreadQueries()
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE 'naplo' ADD COLUMN 'sound_comment_fname' TEXT"
            );
        }
    };

    private static class AdatFeltoltes extends Callback {
        private final String TAG = getClass().getSimpleName();
        Context context;
        public AdatFeltoltes(final Context context) {
            this.context = context;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            dbWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    GyakorlatDao gyakorlatDao = INSTANCE.gyakorlatDao();

                    if(gyakorlatDao.countRows() == 0) {
                        List<Gyakorlat> gyakorlats = loadGyakListFromResource();
                        if (gyakorlats != null) {
                            for (Gyakorlat gy : gyakorlats) {
                                gyakorlatDao.insert(gy);
                            }
                            Log.i(TAG, "run: gyakorlat lista betöltve és feltöltve a db");
                        } else {
                            Log.i(TAG, "run: gyakorlatok betöltése null lett");
                        }
                    }
                }
            });
        }

        private List<Gyakorlat> loadGyakListFromResource() {
            List<Gyakorlat> gyakorlats = null;
            final InputStream gyakstream = context.getApplicationContext().getResources().openRawResource(R.raw.gyaklista);
            try {
                String src = readInputStream(gyakstream);
                gyakorlats = new Gson().fromJson(src, GyakorlatCsomag.class).getGyakorlat();
            } catch (IOException e) {
                Log.e(TAG, "loadGyakListFromResource: resource load error", e);
            }
            return gyakorlats;
        }

        private String readInputStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        }
    }
}
