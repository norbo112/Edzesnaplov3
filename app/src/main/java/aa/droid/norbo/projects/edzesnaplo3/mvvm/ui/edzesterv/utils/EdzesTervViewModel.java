package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv.EdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
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
        if(edzesTerv == null)
            this.edzesTerv = new EdzesTerv(title);
    }

    public void setEdzesTervTitle(String title) {
        if(edzesTerv == null) edzesTerv = new EdzesTerv(title);
        else edzesTerv.setMegnevezes(title);
        notifyEdzesTerv();
    }

    public boolean addEdzesnapForEdzesTerv(Edzesnap edzesnap) {
        if(edzesTerv.getEdzesnapList().contains(edzesnap)) {
            return false;
        }
        edzesTerv.addEdzesNap(edzesnap);

        if(mutableLiveData != null)
            mutableLiveData.postValue(edzesTerv);
        return true;
    }

    /**
     * Edzésnap módosítása, de jelenleg csak egy csoportot lehet vele szerkeszteni, tehát, egyet törlünk, egyet hozzáadunk
     * a modósított gyakorlatokkal vagy plusz gyakorlattal
     * @param edzesnap
     * @return
     */
    public boolean editEdzesnap(Edzesnap edzesnap) {
        if(edzesnap.getValasztottCsoport().size() == 0)
            return false;

        if(edzesTerv.getEdzesnapList().contains(edzesnap)) {
            for(Edzesnap edzesnap1: edzesTerv.getEdzesnapList()) {
                if(edzesnap1.equals(edzesnap)) {
                    if(!edzesnap1.getValasztottCsoport().contains(edzesnap.getValasztottCsoport().get(0))) {
                        edzesnap1.addCsoport(edzesnap.getValasztottCsoport());
                    } else {
                        for(Csoport cs: edzesnap1.getValasztottCsoport()) {
                            if (cs.equals(edzesnap.getValasztottCsoport().get(0))) {
                                Set<GyakorlatTerv> gyakorlatTervSet = new LinkedHashSet<>(cs.getValasztottGyakorlatok());
                                gyakorlatTervSet.addAll(edzesnap.getValasztottCsoport().get(0).getValasztottGyakorlatok());
                                cs.getValasztottGyakorlatok().clear();
                                cs.getValasztottGyakorlatok().addAll(gyakorlatTervSet);
                            }
                        }
                    }
                    notifyEdzesTerv();
                    return true;
                }
            }
        }

        return false;
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
        this.mutableLiveData = null;
    }

    public CompletableFuture<Void> mentes() throws NullPointerException {
        if(edzesTerv == null)
            throw new NullPointerException("Edzésterv nulla");

        return edzesTervRepository.insert(edzesTerv);
    }

    public CompletableFuture<Void> mentes(EdzesTerv edzesTerv) {
        return edzesTervRepository.insert(edzesTerv);
    }

    public LiveData<List<EdzesTervWithEdzesnap>> getEdzestervek() {
        return edzesTervRepository.getTervek();
    }

    public LiveData<EdzesTervWithEdzesnap> getEdzesTervById(int id) {
        return edzesTervRepository.getTervek(id);
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

    public void notifyEdzesTerv() {
        if(mutableLiveData != null)
            mutableLiveData.postValue(edzesTerv);
    }

    public boolean deleteGyakorlatTerv(GyakorlatTerv gyakorlatTerv) {
        boolean oke = false;
        for(Edzesnap edzesnap: edzesTerv.getEdzesnapList()) {
            for (Csoport csoport: edzesnap.getValasztottCsoport()) {
                oke = csoport.getValasztottGyakorlatok().remove(gyakorlatTerv);

                if(csoport.getValasztottGyakorlatok().size() == 0)
                    edzesnap.getValasztottCsoport().remove(csoport);
            }

            if(edzesnap.getValasztottCsoport().size() == 0)
                edzesTerv.getEdzesnapList().remove(edzesnap);
        }

        notifyEdzesTerv();
        return oke;
    }

    public boolean deleteCsoport(Csoport csoport) {
        boolean oke = false;
        for(Edzesnap edzesnap: edzesTerv.getEdzesnapList()) {
            oke = edzesnap.getValasztottCsoport().remove(csoport);

            if(edzesnap.getValasztottCsoport().size() == 0)
                edzesTerv.getEdzesnapList().remove(edzesnap);
        }

        notifyEdzesTerv();
        return oke;
    }

    public boolean deleteEdzesnap(Edzesnap edzesnap) {
        boolean oke = edzesTerv.getEdzesnapList().remove(edzesnap);
        notifyEdzesTerv();
        return oke;
    }
}