package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

@Singleton
public class NaploViewModel extends ViewModel {
    private NaploRepository naploRepository;
    private LiveData<List<Naplo>> naploList;

    @Inject
    public NaploViewModel(NaploRepository naploRepository) {
        this.naploRepository = naploRepository;
        this.naploList = naploRepository.getAll();
    }

    public LiveData<List<Naplo>> getNaploList() {
        return naploList;
    }

    public void deleteNaplo(long naplodatum) {
        naploRepository.delete(naplodatum);
    }

    public LiveData<List<NaploWithSorozat>> getNaploWithSorozat(long naplodatum) {
        return naploRepository.getNaploWithSorozat(naplodatum);
    }

    public LiveData<List<NaploWithSorozat>> getNaploWithSorozat() {
        return naploRepository.getNaploWithSorozat();
    }

    public LiveData<NaploWithOnlySorozats> getNaploWithOnlySorozats(long naplodatum) {
        return naploRepository.getNaploWithOnlySorozat(naplodatum);
    }

    public NaploWithOnlySorozats getSyncNaploWithOnlySorozats(long naplodatum) {
        return naploRepository.getSyncNaploWithOnlySorozats(naplodatum);
    }

    public void insert(Naplo naplo) {
        naploRepository.insert(naplo);
    }
}
