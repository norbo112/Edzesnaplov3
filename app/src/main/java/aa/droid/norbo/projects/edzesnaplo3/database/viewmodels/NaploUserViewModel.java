package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import aa.droid.norbo.projects.edzesnaplo3.database.EdzesNaploDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.NaploUserRepo;

public class NaploUserViewModel extends AndroidViewModel {
    private NaploUserRepo naploUserRepo;
    private LiveData<NaploUser> naploUserLiveData;

    public NaploUserViewModel(@NonNull Application application) {
        super(application);
        naploUserRepo = new NaploUserRepo(application);
        naploUserLiveData = naploUserRepo.getNaploUserLiveData();
    }

    public LiveData<NaploUser> getNaploUserLiveData() {
        return naploUserLiveData;
    }

    public void insert(NaploUser naploUser) {
        naploUserRepo.insert(naploUser);;
    }
}
