package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;

@Singleton
public class LocalSorozatRepository implements SorozatRepository {
    private EdzesNaploDatabase database;
    private ExecutorService executorService;

    @Inject
    public LocalSorozatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        this.database = database;
        this.executorService = executorService;
    }

    @Override
    public void insert(List<Sorozat> sorozat) {
        executorService.execute(() -> database.sorozatDao().insert(sorozat));
    }

    @Override
    public LiveData<List<SorozatWithGyakorlat>> getForNaplo(long naplodatum) {
        return database.sorozatDao().getSorozatWithGyakorlat(Long.toString(naplodatum));
    }
}
