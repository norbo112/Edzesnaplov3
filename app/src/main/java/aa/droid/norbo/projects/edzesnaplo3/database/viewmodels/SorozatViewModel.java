package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.NaploRepo;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.SorozatRepo;

public class SorozatViewModel extends AndroidViewModel {
    private SorozatRepo sorozatRepo;
    private LiveData<List<SorozatWithGyakorlat>> sorozatListLiveData;

    public SorozatViewModel(@NonNull Application application) {
        super(application);
        sorozatRepo = new SorozatRepo(application);
        sorozatListLiveData = sorozatRepo.getSorozatListLiveData();
    }

    public LiveData<List<SorozatWithGyakorlat>> getSorozatListLiveData() {
        return sorozatListLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<LiveData<List<SorozatWithGyakorlat>>> getSorozatWithGyakByNaplo(String naplodatum) {
        return sorozatRepo.getSorozatWithGyakorlatByNaplo(naplodatum);
    }

    public void insert(Sorozat sorozat) {
        sorozatRepo.insert(sorozat);
    }

    public void insert(List<Sorozat> sorozats) {
        sorozatRepo.insert(sorozats);
    }

    public void deleteAll() { sorozatRepo.deleteAll(); }

}
