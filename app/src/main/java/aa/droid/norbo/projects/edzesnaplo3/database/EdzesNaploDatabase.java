package aa.droid.norbo.projects.edzesnaplo3.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.models.GyakorlatCsomag;

@Database(entities = {Gyakorlat.class, Sorozat.class, Naplo.class}, version = 1, exportSchema = false)
public abstract class EdzesNaploDatabase extends RoomDatabase {
    public abstract GyakorlatDao gyakorlatDao();
    public abstract NaploDao naploDao();
    public abstract SorozatDao sorozatDao();

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
                            .addCallback(new GyakorlatBeszurCallback(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class GyakorlatBeszurCallback extends Callback {
        private final String TAG = getClass().getSimpleName();
        Context context;
        public GyakorlatBeszurCallback(final Context context) {
            this.context = context;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            dbWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    GyakorlatDao gyakorlatDao = INSTANCE.gyakorlatDao();

                    if(gyakorlatDao.countRows() != 0) {
                        return;
                    }

                    List<Gyakorlat> gyakorlats = loadGyakListFromResource();
                    if(gyakorlats != null) {
                        for(Gyakorlat gy: gyakorlats) {
                            gyakorlatDao.insert(gy);
                        }
                        Log.i(TAG, "run: gyakorlat lista betöltve és feltöltve a db");
                    } else {
                        Log.i(TAG, "run: gyakorlatok betöltése null lett");
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
