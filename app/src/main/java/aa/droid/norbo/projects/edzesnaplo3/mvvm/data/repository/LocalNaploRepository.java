package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
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
}
