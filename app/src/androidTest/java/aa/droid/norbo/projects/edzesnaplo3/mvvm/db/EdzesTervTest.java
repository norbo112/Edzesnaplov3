package aa.droid.norbo.projects.edzesnaplo3.mvvm.db;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters.TervModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.edzesterv.LocalEdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.CsoportDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesnapDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.GyakorlatTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.fortest.TestEdzesTerv;

@RunWith(AndroidJUnit4.class)
public class EdzesTervTest {
    EdzesTervDao edzesTervDao;
    CsoportDao csoportDao;
    GyakorlatTervDao gyakorlatTervDao;
    EdzesnapDao edzesnapDao;

    private EdzesTervDatabase db;
    private LocalEdzesTervRepository repository;

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EdzesTervDatabase.class).build();
        edzesTervDao = db.edzesTervDao();
        csoportDao = db.csoportDao();
        gyakorlatTervDao = db.gyakorlatTervDao();
        edzesnapDao = db.edzesnapDao();
        repository = new LocalEdzesTervRepository(db, Executors.newSingleThreadExecutor(),
                new TervModelConverter());
    }

    @After
    public void end() throws IOException {
        db.close();
    }

    @Test
    public void createEdzesnap() throws Exception {
        EdzesTerv edzesTerv = TestEdzesTerv.getEdzesTerv("Teszt 1 edzésterv");
        EdzesTerv edzesTerv1 = TestEdzesTerv.getEdzesTerv("Teszt 2 terv");
        CompletableFuture<Void> insert = repository.insert(edzesTerv);

//        EdzesTervEntity edzesTervEntity = new EdzesTervEntity("Első edzésterv");
//        long insertedTerv = edzesTervDao.insert(edzesTervEntity);
//        EdzesnapEntity edzesnapEntity = new EdzesnapEntity((int) insertedTerv, "1.nap");
//        long intertedEdzesnap = edzesnapDao.insert(edzesnapEntity);

        insert.whenComplete((aVoid, throwable) -> {
            Assert.assertNull(throwable);
        });

        List<EdzesTervWithEdzesnap> edzesTervWithEdzesnaps = edzesTervDao.getAllForTest();

        for (EdzesTervWithEdzesnap e: edzesTervWithEdzesnaps) {
            Assert.assertEquals(edzesTerv.getMegnevezes(), e.edzesTervEntity.getMegnevezes());
        }

        Assert.assertEquals(1, edzesTervWithEdzesnaps.size());
    }
}
