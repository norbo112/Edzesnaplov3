package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.CsoportEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesTervEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.EdzesnapEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;

@Singleton
public class TervModelConverter {
    @Inject
    public TervModelConverter() {
    }

    public EdzesTervEntity getEdzesTervEntity(EdzesTerv edzesTerv) {
        return new EdzesTervEntity(edzesTerv.getMegnevezes());
    }

    public EdzesnapEntity getEdzesnapEntity(int tervid, Edzesnap edzesnap) {
        return new EdzesnapEntity(tervid, edzesnap.getEdzesNapLabel());
    }

    /**
     * Sajnos a két lista nem biztos hogy móködni fog az sqlite dbbe...
     * @param csoportid
     * @param gyakorlatTerv
     * @return
     */
    public GyakorlatTervEntity getGyakorlatTervEntity(int edzesnapId, int csoportid, GyakorlatTerv gyakorlatTerv) {
        return new GyakorlatTervEntity(edzesnapId, csoportid, gyakorlatTerv.getMegnevezes(), gyakorlatTerv.getSorozatSzam(), gyakorlatTerv.getIsmetlesSzam());
    }

    public CsoportEntity getCsoportEntity(int edzesnapid, Csoport csoport) {
        return new CsoportEntity(csoport.getIzomcsoport(), edzesnapid);
    }

    public GyakorlatTerv getFromEntity(GyakorlatTervEntity entity) {
        return new GyakorlatTerv(entity.getMegnevezes(), entity.getSorozatSzam(), entity.getIsmetlesSzam());
    }
}
