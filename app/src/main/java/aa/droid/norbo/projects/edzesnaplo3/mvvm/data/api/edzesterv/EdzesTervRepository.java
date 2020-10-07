package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv;

import java.util.concurrent.CompletableFuture;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;

public interface EdzesTervRepository {
    CompletableFuture<Void> insert(EdzesTerv edzesTerv);
}
