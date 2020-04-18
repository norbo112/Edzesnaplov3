package aa.droid.norbo.projects.edzesnaplo3.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.tapanyag.ElelmiszerDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.ElelmiszerCsomag;

@Database(entities = {Elelmiszer.class}, version = 1)
public abstract class TapanyagDatabase extends RoomDatabase {
    public abstract ElelmiszerDao elelmiszerDao();

    public static volatile TapanyagDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService dbWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TapanyagDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TapanyagDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TapanyagDatabase.class, "tapanyagdb_db")
                            .addCallback(new AdatFeltoltes(context))
                            .build();
                }
            }
        }

        return INSTANCE;
    }

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
                    ElelmiszerDao elelmiszerDao = INSTANCE.elelmiszerDao();

                    if(elelmiszerDao.countRows() == 0) {
                        List<Elelmiszer> elelmiszers = loadElelmiszerFromSrc();
                        if(elelmiszers != null) {
                            elelmiszerDao.insert(elelmiszers);
                            Log.i(TAG, "run: élelmiszerek feltöltve az adatbázosba : "+elelmiszers.size()+" db");
                        } else {
                            Log.i(TAG, "run: élelmiszerek betöltése null lett");
                        }
                    }
                }
            });
        }

        private List<Elelmiszer> loadElelmiszerFromSrc() {
            List<Elelmiszer> elelmiszers = null;
            final InputStream elelmiszerstream = context.getApplicationContext().getResources().openRawResource(R.raw.tapanyagok);
            try {
                String src = readInputStream(elelmiszerstream);
                elelmiszers = new Gson().fromJson(src, ElelmiszerCsomag.class).getTapanyagok();
                Log.i(TAG, "loadElelmiszerFromSrc: elelmiszerek betöltve az erőforrásból");
            } catch (IOException e) {
                Log.e(TAG, "loadElelmiszerFromSrc: resource load error", e);
            }
            return elelmiszers;
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
