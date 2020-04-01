package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.GyakorlatRepo;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.NaploRepo;

public class NaploViewModel extends AndroidViewModel {
    private NaploRepo naploRepo;
    private LiveData<List<Naplo>> naploListLiveData;

    public NaploViewModel(@NonNull Application application) {
        super(application);
        naploRepo = new NaploRepo(application);
        naploListLiveData = naploRepo.getNaploListLiveData();
    }

    public LiveData<List<Naplo>> getNaploListLiveData() {
        return naploListLiveData;
    }

    public void insert(Naplo naplo) {
        naploRepo.insert(naplo);
    }

    public void delete(Naplo naplo) { naploRepo.delete(naplo); }

    public void update(Naplo naplo) {
        naploRepo.update(naplo);
    }

    public void deleteAll() { naploRepo.deleteAll(); }
}
