package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;

public interface EdzesTervRepository {
    CompletableFuture<Void> insert(EdzesTerv edzesTerv);

    LiveData<List<EdzesTervWithEdzesnap>> getTervek();

    CompletableFuture<Void> deleteTerv(int tervId);
}
