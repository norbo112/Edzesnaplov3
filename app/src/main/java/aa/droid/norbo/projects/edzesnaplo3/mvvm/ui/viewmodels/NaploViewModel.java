package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import dagger.hilt.android.scopes.ActivityScoped;

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
}
