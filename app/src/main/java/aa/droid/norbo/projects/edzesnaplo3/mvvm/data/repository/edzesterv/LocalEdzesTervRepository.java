package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.edzesterv;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv.EdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters.TervModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.EdzesTervDatabase;

@Singleton
public class LocalEdzesTervRepository implements EdzesTervRepository {
    private EdzesTervDatabase database;
    private ExecutorService executorService;
    private TervModelConverter modelConverter;

    @Inject
    public LocalEdzesTervRepository(EdzesTervDatabase database, ExecutorService executorService, TervModelConverter modelConverter) {
        this.database = database;
        this.executorService = executorService;
        this.modelConverter = modelConverter;
    }

    @Override
    public CompletableFuture<Void> insert(EdzesTerv edzesTerv) {
        return CompletableFuture.runAsync(() -> executorService.execute(() -> database.runInTransaction(() -> {
            long tervid = database.edzesTervDao().insert(modelConverter.getEdzesTervEntity(edzesTerv));
            for (Edzesnap edzesnap : edzesTerv.getEdzesnapList()) {
                database.edzesnapDao().insert(modelConverter.getEdzesnapEntity((int) tervid, edzesnap));
                List<Csoport> valasztottCsoport = edzesnap.getValasztottCsoport();
                for (Csoport csoport: valasztottCsoport) {
                    long csoportid = database.csoportDao().insert(modelConverter.getCsoportEntity(csoport));
                    for (GyakorlatTerv gyakorlatTerv: csoport.getValasztottGyakorlatok()) {
                        database.gyakorlatTervDao().insert(modelConverter.getGyakorlatTervEntity((int) tervid, (int) csoportid, gyakorlatTerv));
                    }
                }
            }
        })), executorService);
    }
}
