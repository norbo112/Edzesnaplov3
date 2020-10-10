package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.GyakorlatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;

@Singleton
public class GyakorlatViewModel extends ViewModel {
    private GyakorlatRepository gyakorlatRepository;

    @Inject
    public GyakorlatViewModel(GyakorlatRepository gyakorlatRepository) {
        this.gyakorlatRepository = gyakorlatRepository;
    }

    public LiveData<List<Gyakorlat>> getGyakorlatList() {
        return gyakorlatRepository.getAll();
    }

    public void delete(Gyakorlat gyakorlat) {
        gyakorlatRepository.delete(gyakorlat);
    }

    public void insert(Gyakorlat gyakorlat) {
        gyakorlatRepository.insert(gyakorlat);
    }

    public void update(Gyakorlat gyakorlat) {
        gyakorlatRepository.update(gyakorlat);
    }

    public LiveData<List<Gyakorlat>> getGyakorlatByCsoport(List<String> csoportok) {
        return gyakorlatRepository.getByCsoport(csoportok);
    }
}
