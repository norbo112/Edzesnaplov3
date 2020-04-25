package aa.droid.norbo.projects.edzesnaplo3.database.repositorys;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatDao;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlatAndNaplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class SorozatRepo {
    private SorozatDao sorozatDao;
    private LiveData<List<SorozatWithGyakorlat>> sorozatListLiveData;
    private LiveData<List<Sorozat>> allSorozat;
    private LiveData<List<SorozatWithGyakorlatAndNaplo>> allSorozatWithNaplo;

    public SorozatRepo(Application application) {
        EdzesNaploDatabase db = EdzesNaploDatabase.getDatabase(application);
        sorozatDao = db.sorozatDao();
        sorozatListLiveData = sorozatDao.getAllSorozat();
        allSorozat = sorozatDao.getall();
        allSorozatWithNaplo = sorozatDao.getAllWithNaplo();
    }

    public LiveData<List<SorozatWithGyakorlatAndNaplo>> getAllSorozatWithNaplo() {
        return allSorozatWithNaplo;
    }

    public LiveData<List<SorozatWithGyakorlat>> getSorozatListLiveData() {
        return sorozatListLiveData;
    }

    public LiveData<List<Sorozat>> getAllSorozat() {
        return allSorozat;
    }

    public List<Sorozat> getallByGyakorlat(int gyakid) {
        return sorozatDao.getallByGyakorlat(gyakid);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<LiveData<List<SorozatWithGyakorlat>>> getSorozatWithGyakorlatByNaplo(String naplodatum) {
        return CompletableFuture.supplyAsync(new Supplier<LiveData<List<SorozatWithGyakorlat>>>() {
            @Override
            public LiveData<List<SorozatWithGyakorlat>> get() {
                return sorozatDao.getSorozatWithGyakorlat(naplodatum);
            }
        }, EdzesNaploDatabase.dbWriteExecutor);
    }

    public List<SorozatWithGyakorlat> getSorozatWithGyakorlatByNaploToList(String naplodatum) {
        return sorozatDao.getSorozatWithGyakorlatToList(naplodatum);
    }

    public void insert(Sorozat sorozat) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sorozatDao.insert(sorozat);
            }
        });
    }

    public void insert(List<Sorozat> sorozats) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sorozatDao.insert(sorozats);
            }
        });
    }

    public void deleteAll() {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sorozatDao.deleteAll();
            }
        });
    }

    public void delete(String naplodatum) {
        EdzesNaploDatabase.dbWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sorozatDao.delete(naplodatum);
            }
        });
    }

    public int getOsszSulyByNaplo(String naplodatum) {
        return sorozatDao.getOsszSulyByNaplo(naplodatum);
    }

    public int getSorozatKorabbiOsszsuly(int gyakid) {
        return sorozatDao.getSorozatKorabbiOsszsuly(gyakid);
    }
}
