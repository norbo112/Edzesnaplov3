package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.GyakorlatRepo;

public class GyakorlatViewModel extends AndroidViewModel {
    private GyakorlatRepo gyakorlatRepo;
    private LiveData<List<Gyakorlat>> gyListLiveData;

    public GyakorlatViewModel(@NonNull Application application) {
        super(application);
        gyakorlatRepo = new GyakorlatRepo(application);
        gyListLiveData = gyakorlatRepo.getGyakorlatLiveData();
    }

    public LiveData<List<Gyakorlat>> getGyListLiveData() {
        return gyListLiveData;
    }

    public void insert(Gyakorlat gyakorlat) {
        gyakorlatRepo.insert(gyakorlat);
    }

    public void delete(Gyakorlat gyakorlat) { gyakorlatRepo.delete(gyakorlat); }

    public void update(Gyakorlat gyakorlat) {
        gyakorlatRepo.update(gyakorlat);
    }
}
