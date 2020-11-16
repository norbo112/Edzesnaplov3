package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.OsszSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;


@Singleton
public class SorozatViewModel extends ViewModel {
    private SorozatRepository sorozatRepository;
    private LiveData<List<Sorozat>> sorozats;

    @Inject
    public SorozatViewModel(SorozatRepository sorozatRepository) {
        this.sorozatRepository = sorozatRepository;
        this.sorozats = sorozatRepository.getAll();
    }

    public LiveData<List<Sorozat>> getSorozats() {
        return sorozats;
    }

    public LiveData<List<SorozatWithGyakorlat>> getForNaplo(long naplodatum) {
        return sorozatRepository.getForNaplo(naplodatum);
    }

    public LiveData<List<Sorozat>> getSorozatByGyakorlat(int gyakorlatid) {
        return sorozatRepository.getSorozatByGyakorlat(gyakorlatid);
    }

    public LiveData<List<OsszSorozat>> getOsszSorozatByGyakorlat(int gyakid) {
        return sorozatRepository.getOsszSorozatByGyakorlat(gyakid);
    }

    public void deleteSorozat(long naplodatum) {
        sorozatRepository.deleteSorozatByNaplo(naplodatum);
    }

    public void insertAll(List<Sorozat> sorozats) {
        sorozatRepository.insert(sorozats);
    }

    public void insert(Sorozat sorozat) { sorozatRepository.insert(sorozat);}
}
