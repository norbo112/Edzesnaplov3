package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

@Singleton
public class NaploViewModel extends ViewModel {
    private EdzesNaploDatabase database;
    private ExecutorService executorService;
    private LiveData<List<Naplo>> naploList;

    @Inject
    public NaploViewModel(EdzesNaploDatabase database, ExecutorService executorService) {
        this.database = database;
        this.executorService = executorService;
        this.naploList = database.naploDao().getAll();
    }

    public LiveData<List<Naplo>> getNaploList() {
        return naploList;
    }
}
