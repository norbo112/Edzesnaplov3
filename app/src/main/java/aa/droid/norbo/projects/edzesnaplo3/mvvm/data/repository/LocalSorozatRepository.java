package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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

    private LiveData<List<Sorozat>> sorozatLista;

    @Inject
    public LocalSorozatRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        this.database = database;
        this.executorService = executorService;
        this.sorozatLista = database.sorozatDao().getall();
    }

    @Override
    public void insert(List<Sorozat> sorozat) {
        executorService.execute(() -> database.sorozatDao().insert(sorozat));
    }

    @Override
    public LiveData<List<SorozatWithGyakorlat>> getForNaplo(long naplodatum) {
        return database.sorozatDao().getSorozatWithGyakorlat(naplodatum);
    }

    @Override
    public LiveData<List<Sorozat>> getAll() {
        return sorozatLista;
    }

    @Override
    public LiveData<List<Sorozat>> getSorozatByGyakorlat(int gyakorlatid) {
        return database.sorozatDao().getallByGyakorlat(gyakorlatid);
    }

    @Override
    public void deleteSorozatByNaplo(long naplodatum) {
        executorService.execute(() -> database.sorozatDao().delete(Long.toString(naplodatum)));
    }

    @Override
    public void insert(Sorozat sorozat) {
        executorService.execute(() -> database.sorozatDao().insert(sorozat));
    }
}
