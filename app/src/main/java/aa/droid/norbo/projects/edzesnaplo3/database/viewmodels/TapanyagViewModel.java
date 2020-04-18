package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.tapanyag.TapanyagRepo;

public class TapanyagViewModel extends AndroidViewModel {
    private TapanyagRepo tapanyagRepo;
    private LiveData<List<Elelmiszer>> liveData;
    private LiveData<List<String>> elelemTipus;

    public TapanyagViewModel(@NonNull Application application) {
        super(application);
        tapanyagRepo = new TapanyagRepo(application);
        liveData = tapanyagRepo.getLiveData();
        elelemTipus = tapanyagRepo.getElelemTipus();
    }

    public LiveData<List<Elelmiszer>> getLiveData() {
        return liveData;
    }

    public LiveData<List<String>> getElelemTipus() {
        return elelemTipus;
    }

    public void insert(List<Elelmiszer> elelmiszers) {
        tapanyagRepo.insert(elelmiszers);
    }

    public void deleteAll() {
        tapanyagRepo.deleteAll();
    }
}
