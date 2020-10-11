package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv.EdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;

@Singleton
public class EdzesTervViewModel extends ViewModel {
    private EdzesTervRepository edzesTervRepository;
    private EdzesTerv edzesTerv;
    private MutableLiveData<EdzesTerv> mutableLiveData;

    @Inject
    public EdzesTervViewModel(EdzesTervRepository edzesTervRepository) {
        this.edzesTervRepository = edzesTervRepository;
    }

    public void createEdzesTerv(String title) {
        this.edzesTerv = new EdzesTerv(title);
    }

    public boolean addEdzesnapForEdzesTerv(Edzesnap edzesnap) {
        if(edzesTerv.getEdzesnapList().contains(edzesnap)) {
            return false;
        }
        edzesTerv.addEdzesNap(edzesnap);

        if(mutableLiveData != null)
            mutableLiveData.postValue(edzesTerv);
        return true;
//        EdzesTerv edzesTerv = mutableLiveData.getValue();
//        if(edzesTerv.getEdzesnapList().contains(edzesnap)) {
//            return false;
//        }
//        edzesTerv.addEdzesNap(edzesnap);
//        mutableLiveData.postValue(edzesTerv);
    }

    public boolean isEdzesnapInEdzesterv(String edzesnapLabel) {
        for(Edzesnap edzesnap: edzesTerv.getEdzesnapList()) {
            if(edzesnap.getEdzesNapLabel().equals(edzesnapLabel))
                return true;
        }
        return false;
    }

    public EdzesTerv getEdzesTerv() {
        return edzesTerv;
    }

    public void clear() {
        this.edzesTerv = null;
        mutableLiveData.postValue(edzesTerv);
    }

    public CompletableFuture<Void> mentes() throws NullPointerException {
        if(edzesTerv == null)
            throw new NullPointerException("Edzésterv nulla");

        return edzesTervRepository.insert(edzesTerv);
    }

    public LiveData<List<EdzesTervWithEdzesnap>> getEdzestervek() {
        return edzesTervRepository.getTervek();
    }

    public CompletableFuture<Void> deleteTerv(int tervId) {
        return edzesTervRepository.deleteTerv(tervId);
    }

    public LiveData<EdzesTerv> getEdzesTervLiveData() {
        if(mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(edzesTerv);
        }
        return mutableLiveData;
    }
}
