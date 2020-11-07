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
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploCntAppWidget;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class NaploWorker {
    private static final String TAG = "NaploWorker";
    private Context context;
    private Naplo naplo;
    private Gyakorlat gyakorlat;
    private List<Sorozat> sorozats;
    private MutableLiveData<List<Sorozat>> liveSorozatLista;
    private MutableLiveData<Integer> gyakorlatSzam;
    private NaploRepository naploRepository;
    private SorozatRepository sorozatRepository;

    @Inject
    public NaploWorker(NaploRepository naploRepository, SorozatRepository sorozatRepository, @ActivityContext Context context) {
        this.naploRepository = naploRepository;
        this.sorozatRepository = sorozatRepository;
        this.naplo = new Naplo(System.currentTimeMillis(), "TestÜzem");
        this.sorozats = new ArrayList<>();
        this.liveSorozatLista = new MutableLiveData<>();
        this.gyakorlatSzam = new MutableLiveData<>();
        this.context = context;
    }

    public void addSorozat(int suly, int ism) {
        if(gyakorlat != null) {
            sorozats.add(new Sorozat(gyakorlat, suly, ism, System.currentTimeMillis(), naplo.getNaplodatum()));
        }

        liveSorozatLista.postValue(sorozats);
    }

    public void setGyakorlat(Gyakorlat gyakorlat) {
        this.gyakorlat = gyakorlat;
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

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, NaploCntAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);
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
