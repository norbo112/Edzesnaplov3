package aa.droid.norbo.projects.edzesnaplo3.database.repositorys;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.GyakorlatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class NaploRepo {
    private NaploDao naploDao;
    private LiveData<List<Naplo>> naploListLiveData;

    public NaploRepo(Application application) {
        EdzesNaploDatabase db = EdzesNaploDatabase.getDatabase(application);
        naploDao = db.naploDao();
        naploListLiveData = naploDao.getNaploLiveData();
    }

    public LiveData<List<Naplo>> getNaploListLiveData() {
        return naploListLiveData;
    }

    public void insert(Naplo naplo) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                naploDao.insertNaplo(naplo);
            }
        });
    }

    public void update(Naplo naplo) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                naploDao.updateNaplo(naplo);
            }
        });
    }

    public void delete(Naplo naplo) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                naploDao.deleteNaplo(naplo);
            }
        });
    }
}
