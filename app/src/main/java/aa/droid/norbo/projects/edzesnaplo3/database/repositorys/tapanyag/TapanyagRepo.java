package aa.droid.norbo.projects.edzesnaplo3.database.repositorys.tapanyag;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.TapanyagDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.tapanyag.ElelmiszerDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;

public class TapanyagRepo {
    private ElelmiszerDao elelmiszerDao;
    private LiveData<List<Elelmiszer>> liveData;
    private LiveData<List<String>> elelemTipus;

    public TapanyagRepo(Application application) {
        TapanyagDatabase db = TapanyagDatabase.getDatabase(application);
        elelmiszerDao = db.elelmiszerDao();
        liveData = elelmiszerDao.getLiveData();
        elelemTipus = elelmiszerDao.getElelmiszerTipus();
    }

    public LiveData<List<Elelmiszer>> getLiveData() {
        return liveData;
    }

    public LiveData<List<String>> getElelemTipus() {
        return elelemTipus;
    }

    public void insert(List<Elelmiszer> elelmiszers) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                elelmiszerDao.insert(elelmiszers);
            }
        });
    }

    public void deleteAll() {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                elelmiszerDao.deleteAll();
            }
        });
    }
}
