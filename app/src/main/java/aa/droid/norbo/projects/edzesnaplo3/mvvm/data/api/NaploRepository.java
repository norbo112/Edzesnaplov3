package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api;

import android.database.Cursor;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

public interface NaploRepository {
    void insert(Naplo naplo);
    LiveData<List<Naplo>> getAll();

    void delete(long naplodatum);

    LiveData<List<NaploWithSorozat>> getNaploWithSorozat(long naplodatum);

    LiveData<List<NaploWithSorozat>> getNaploWithSorozat();

    LiveData<NaploWithOnlySorozats> getNaploWithOnlySorozat(long naplodatum);

    NaploWithOnlySorozats getSyncNaploWithOnlySorozats(long naplodatum);

    Cursor getNaploList();

    Cursor getNaploOsszSulyBy();

    LiveData<Naplo> getNaploByNaploDatum(long naplodatum);

    List<NaploWithSorozat> getNaploWithSorozatList();
}
