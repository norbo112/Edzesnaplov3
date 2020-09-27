package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;

public interface SorozatRepository {
    void insert(List<Sorozat> sorozat);
    LiveData<List<SorozatWithGyakorlat>> getForNaplo(long naplodatum);
}
