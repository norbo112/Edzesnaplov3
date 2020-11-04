package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;

public interface GyakorlatRepository {
    LiveData<List<Gyakorlat>> getAll();

    LiveData<List<Gyakorlat>> getByCsoport(List<String> izomcsoportok);

    void delete(Gyakorlat gyakorlat);

    void insert(Gyakorlat gyakorlat);

    void update(Gyakorlat gyakorlat);

    CompletableFuture<List<Gyakorlat>> getGyakorlatList();
}
