package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api;

import androidx.lifecycle.LiveData;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;

public interface NaploRepository {
    void insert(Naplo naplo);
    LiveData<List<Naplo>> getAll();
}
