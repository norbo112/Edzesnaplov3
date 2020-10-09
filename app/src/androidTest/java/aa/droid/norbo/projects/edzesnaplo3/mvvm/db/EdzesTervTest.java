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
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.repository.edzesterv.LocalEdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.CsoportDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.EdzesnapDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.GyakorlatTervDao;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithGyakorlatTervek;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.fortest.TestEdzesTerv;

@RunWith(AndroidJUnit4.class)
public class EdzesTervTest {
    EdzesTervDao edzesTervDao;
    CsoportDao csoportDao;
    GyakorlatTervDao gyakorlatTervDao;
    EdzesnapDao edzesnapDao;

    private EdzesTervDatabase db;
    private TervModelConverter modelConverter;

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EdzesTervDatabase.class).build();
        edzesTervDao = db.edzesTervDao();
        csoportDao = db.csoportDao();
        gyakorlatTervDao = db.gyakorlatTervDao();
        edzesnapDao = db.edzesnapDao();
        modelConverter = new TervModelConverter();



    }

    @After
    public void end() throws IOException {
        db.close();
    }

    @Test
    public void createEdzesnap() throws Exception {
        EdzesTerv edzesTerv = TestEdzesTerv.getEdzesTerv("Teszt 1 edzésterv");

        long insertedEdzesterv = edzesTervDao.insert(modelConverter.getEdzesTervEntity(edzesTerv));
        for (Edzesnap edzesnap : edzesTerv.getEdzesnapList()) {
            edzesnapDao.insert(modelConverter.getEdzesnapEntity((int) insertedEdzesterv, edzesnap));
            List<Csoport> valasztottCsoport = edzesnap.getValasztottCsoport();
            for (Csoport csoport: valasztottCsoport) {
                long csoportid = csoportDao.insert(modelConverter.getCsoportEntity(csoport));
                for (GyakorlatTerv gyakorlatTerv: csoport.getValasztottGyakorlatok()) {
                    gyakorlatTervDao.insert(modelConverter.getGyakorlatTervEntity((int) insertedEdzesterv, (int) csoportid, gyakorlatTerv));
                }
            }
        }

        List<EdzesTervWithEdzesnap> edzesTervWithEdzesnaps = edzesTervDao.getAllForTest();

        Assert.assertEquals(1, edzesTervWithEdzesnaps.size());
        Assert.assertEquals(3, edzesTervWithEdzesnaps.get(0).edzesnapList.size());

        Assert.assertEquals("3.nap pihenő", edzesTervWithEdzesnaps.get(0).edzesnapList.get(2).edzesnapEntity.getEdzesNapLabel());

        List<EdzesTervWithGyakorlatTervek> gyakorlatTerveks = edzesTervDao.getAllGyakorlatForEdzestervToTest();
        Assert.assertEquals(8, gyakorlatTerveks.get(0).gyakorlatTervEntity.size());
    }
}
