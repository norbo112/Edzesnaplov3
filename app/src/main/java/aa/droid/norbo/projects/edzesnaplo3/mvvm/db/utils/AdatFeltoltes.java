package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.fileworkers.model.GyakorlatCsomag;

@Singleton
public class AdatFeltoltes {
    private static final String TAG = "AdatFeltoltes";
    private ExecutorService executorService;
    private EdzesNaploDatabase database;
    private Context context;

    @Inject
    public AdatFeltoltes(ExecutorService executorService, EdzesNaploDatabase database, Context context) {
        this.executorService = executorService;
        this.database = database;
        this.context = context;
    }

    public void gyakorlatAdatFeltoltes() {
        executorService.execute(() -> {
            GyakorlatDao gyakorlatDao = database.gyakorlatDao();

            if(gyakorlatDao.countRows() == 0) {
                List<Gyakorlat> gyakorlats = loadGyakListFromResource();
                if (gyakorlats != null) {
                    for (Gyakorlat gy : gyakorlats) {
                        gyakorlatDao.insert(gy);
                    }

                    Log.i(TAG, "gyakorlatAdatFeltoltes: Gyakorlatok feltöltése sikeres volt!");
                } else {
                    Log.i(TAG, "Hiba történt a gyakorlatok betöltése közben - loadGyakListFromResource");
                }
            } else {
                Log.i(TAG, "gyakorlatAdatFeltoltes: gyakorlatok már bevannak töltve");
            }
        });
    }

    private List<Gyakorlat> loadGyakListFromResource() {
        List<Gyakorlat> gyakorlats = null;
        final InputStream gyakstream = context.getResources().openRawResource(R.raw.gyaklista);
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