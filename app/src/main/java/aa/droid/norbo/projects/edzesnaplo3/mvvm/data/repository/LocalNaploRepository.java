package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

@Singleton
public class LocalNaploRepository implements NaploRepository {
    private EdzesNaploDatabase database;
    private ExecutorService executorService;

    @Inject
    public LocalNaploRepository(EdzesNaploDatabase database, ExecutorService executorService) {
        this.database = database;
        this.executorService = executorService;
    }

    @Override
    public void insert(Naplo naplo) {
        executorService.execute(() -> database.naploDao().insertNaplo(naplo));
    }

    @Override
    public LiveData<List<Naplo>> getAll() {
        return database.naploDao().getAll();
    }

    @Override
    public void delete(long naplodatum) {
        executorService.execute(() -> database.naploDao().delete(naplodatum));
    }

    @Override
    public LiveData<List<NaploWithSorozat>> getNaploWithSorozat(long naplodatum) {
        return database.naploDao().getNaploWithSorozats(naplodatum);
    }

    @Override
    public LiveData<List<NaploWithSorozat>> getNaploWithSorozat() {
        return database.naploDao().getNaploWithSorozats();
    }

    @Override
    public LiveData<NaploWithOnlySorozats> getNaploWithOnlySorozat(long naplodatum) {
        return database.naploDao().getNaploWithOnlySorozats(naplodatum);
    }

    @Override
    public NaploWithOnlySorozats getSyncNaploWithOnlySorozats(long naplodatum) {
        return database.naploDao().getSyncNaploWithOnlySorozats(naplodatum);
    }
}
