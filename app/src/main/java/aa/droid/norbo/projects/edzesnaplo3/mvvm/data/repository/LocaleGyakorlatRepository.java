package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.GyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;

@Singleton
public class LocaleGyakorlatRepository implements GyakorlatRepository {
    private EdzesNaploDatabase database;
    private ExecutorService executorService;

    @Inject
    public LocaleGyakorlatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        this.database = database;
        this.executorService = executorService;
    }

    @Override
    public LiveData<List<Gyakorlat>> getAll() {
        return database.gyakorlatDao().getGyakorlatLiveData();
    }

    @Override
    public void delete(Gyakorlat gyakorlat) {
        executorService.execute(() -> database.gyakorlatDao().deleteGyakorlat(gyakorlat));
    }

    @Override
    public void insert(Gyakorlat gyakorlat) {
        executorService.execute(() -> database.gyakorlatDao().insert(gyakorlat));
    }

    @Override
    public void update(Gyakorlat gyakorlat) {
        executorService.execute(() -> database.gyakorlatDao().updateGyakorlat(gyakorlat));
    }
}
