package aa.droid.norbo.projects.edzesnaplo3.database.repositorys;

import android.app.Application;

import androidx.lifecycle.LiveData;

import aa.droid.norbo.projects.edzesnaplo3.Edzes;
import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploUserDao;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;

public class NaploUserRepo {
    private NaploUserDao naploUserDao;
    private LiveData<NaploUser> naploUserLiveData;

    public NaploUserRepo(Application application) {
        EdzesNaploDatabase db = EdzesNaploDatabase.getDatabase(application);
        naploUserDao = db.naploUserDao();
        naploUserLiveData = naploUserDao.getNaploUser();
    }

    public LiveData<NaploUser> getNaploUserLiveData() {
        return naploUserLiveData;
    }

    public void insert(NaploUser naploUser) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                naploUserDao.insert(naploUser);
            }
        });
    }
}
