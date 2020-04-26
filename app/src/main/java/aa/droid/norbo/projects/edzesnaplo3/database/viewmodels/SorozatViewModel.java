package aa.droid.norbo.projects.edzesnaplo3.database.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatGyakorlatNevvel;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlatAndNaplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.repositorys.SorozatRepo;

public class SorozatViewModel extends AndroidViewModel {
    private SorozatRepo sorozatRepo;
    private LiveData<List<SorozatWithGyakorlat>> sorozatListLiveData;
    private LiveData<List<Sorozat>> allSorozat;
    private LiveData<List<SorozatWithGyakorlatAndNaplo>> allSorozatWithNaplo;

    public SorozatViewModel(@NonNull Application application) {
        super(application);
        sorozatRepo = new SorozatRepo(application);
        sorozatListLiveData = sorozatRepo.getSorozatListLiveData();
        allSorozat = sorozatRepo.getAllSorozat();
        allSorozatWithNaplo = sorozatRepo.getAllSorozatWithNaplo();
    }

    public LiveData<List<SorozatWithGyakorlatAndNaplo>> getAllSorozatWithNaplo() {
        return allSorozatWithNaplo;
    }

    public LiveData<List<SorozatWithGyakorlat>> getSorozatListLiveData() {
        return sorozatListLiveData;
    }

    public LiveData<List<Sorozat>> getAllSorozat() {
        return allSorozat;
    }

    public List<Sorozat> getallByGyakorlat(int gyakid) {
        return sorozatRepo.getallByGyakorlat(gyakid);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<LiveData<List<SorozatWithGyakorlat>>> getSorozatWithGyakByNaplo(String naplodatum) {
        return sorozatRepo.getSorozatWithGyakorlatByNaplo(naplodatum);
    }

    /**
     * Sorozat lista betöltése a napló dátuma lapján, asyncron modon
     * @param naplodatum
     * @return
     */
    public List<SorozatWithGyakorlat> getSorozatWithGyakByNaploToList(String naplodatum) {
        return sorozatRepo.getSorozatWithGyakorlatByNaploToList(naplodatum);
    }

    public void insert(Sorozat sorozat) {
        sorozatRepo.insert(sorozat);
    }

    public void insert(List<Sorozat> sorozats) {
        sorozatRepo.insert(sorozats);
    }

    public void deleteAll() { sorozatRepo.deleteAll(); }

    public void delete(String naplodatum) { sorozatRepo.delete(naplodatum); }

    public int getOsszSulyByNaplo(String naplodatum) {
        return sorozatRepo.getOsszSulyByNaplo(naplodatum);
    }

    public int getSorozatKorabbiOsszsuly(int gyakid) {
        return sorozatRepo.getSorozatKorabbiOsszsuly(gyakid);
    }
}
