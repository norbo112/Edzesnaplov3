package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.SorozatRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.WidgetUtil;
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploCntAppWidget;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class NaploWorker {
    private Naplo naplo;
    private List<Sorozat> sorozats;
    private MutableLiveData<List<Sorozat>> liveSorozatLista;
    private MutableLiveData<Integer> gyakorlatSzam;
    private NaploRepository naploRepository;
    private SorozatRepository sorozatRepository;
    private WidgetUtil widgetUtil;

    @Inject
    public NaploWorker(NaploRepository naploRepository, SorozatRepository sorozatRepository, WidgetUtil widgetUtil) {
        this.naploRepository = naploRepository;
        this.sorozatRepository = sorozatRepository;
        this.naplo = new Naplo(System.currentTimeMillis(), "TestÜzem");
        this.sorozats = new ArrayList<>();
        this.liveSorozatLista = new MutableLiveData<>();
        this.gyakorlatSzam = new MutableLiveData<>();
        this.widgetUtil = widgetUtil;
    }

    public void addSorozat(Gyakorlat gyakorlat, int suly, int ism, String szettek) {
        sorozats.add(new Sorozat(gyakorlat, suly, ism, System.currentTimeMillis(), naplo.getNaplodatum(), szettek));
        liveSorozatLista.postValue(sorozats);
    }

    public void prepareUjGyakorlat() {
        if(sorozats.size() > 0) {
            naplo.addAllSorozat(sorozats);
            sorozats.clear();
            liveSorozatLista.postValue(new ArrayList<>());
            gyakorlatSzam.postValue(checkGyakSzam());
        }
    }

    public void saveNaplo() {
        naplo.addAllSorozat(sorozats);
        naploRepository.insert(naplo);
        sorozatRepository.insert(naplo.getSorozats());

        widgetUtil.updateWidget();
    }

    public LiveData<List<Sorozat>> getLiveSorozatLista() {
        return liveSorozatLista;
    }

    public int getSorozatOsszSuly() {
        return (int) sorozats.stream().mapToDouble(sorozat -> sorozat.getSuly() * sorozat.getIsmetles()).sum();
    }

    public Naplo getNaplo() {
        return naplo;
    }

    public int checkGyakSzam() {
        Set<String> gy = new HashSet<>();
        List<Sorozat> sorozats = naplo.getSorozats();
        for(Sorozat sorozat: sorozats) {
            gy.add(sorozat.getGyakorlat().getMegnevezes());
        }
        return gy.size();
    }

    public LiveData<Integer> getGyakorlatSzam() {
        return gyakorlatSzam;
    }

    public void setSorozat(int position, Sorozat sorozat) {
        sorozats.set(position, sorozat);
        liveSorozatLista.postValue(sorozats);
    }
}
