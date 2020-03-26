package aa.droid.norbo.projects.edzesnaplo3.database.repositorys;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

public class GyakorlatRepo {
    private GyakorlatDao gyakorlatDao;
    private LiveData<List<Gyakorlat>> gyakorlatLiveData;

    public GyakorlatRepo(Application application) {
        EdzesNaploDatabase db = EdzesNaploDatabase.getDatabase(application);
        gyakorlatDao = db.gyakorlatDao();
        gyakorlatLiveData = gyakorlatDao.getGyakorlatLiveData();
    }

    public LiveData<List<Gyakorlat>> getGyakorlatLiveData() {
        return gyakorlatLiveData;
    }

    public void insert(Gyakorlat gyakorlat) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gyakorlatDao.insert(gyakorlat);
            }
        });
    }

    public void update(Gyakorlat gyakorlat) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gyakorlatDao.updateGyakorlat(gyakorlat);
            }
        });
    }

    public void delete(Gyakorlat gyakorlat) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gyakorlatDao.deleteGyakorlat(gyakorlat);
            }
        });
    }
}
