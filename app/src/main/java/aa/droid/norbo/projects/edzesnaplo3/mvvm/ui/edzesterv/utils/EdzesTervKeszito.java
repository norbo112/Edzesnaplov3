package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters.TervModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.CsoportWithGyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesnapWithCsoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;

@Singleton
public class EdzesTervKeszito {
    private TervModelConverter tervModelConverter;

    @Inject
    public EdzesTervKeszito(TervModelConverter tervModelConverter) {
        this.tervModelConverter = tervModelConverter;
    }

    /**
     * Room Daoból megkapott EdzesTervWithEdzesnap osztályt alakítja át megjeleníthető EdzesTerv osztállyá
     * @param edzesTervWithEdzesnaps
     * @return
     */
    public List<EdzesTerv> makeEdzesterv(List<EdzesTervWithEdzesnap> edzesTervWithEdzesnaps) {
        List<EdzesTerv> edzesTervs = new ArrayList<>();

        for (EdzesTervWithEdzesnap edzesTervWithEdzesnap : edzesTervWithEdzesnaps) {
            EdzesTerv edzesTerv = new EdzesTerv(edzesTervWithEdzesnap.edzesTervEntity.getMegnevezes());
            edzesTerv.setTervId(edzesTervWithEdzesnap.edzesTervEntity.getId());
            for (EdzesnapWithCsoport eddzesnap : edzesTervWithEdzesnap.edzesnapList) {
                List<String> csoport = eddzesnap.csoportsWithGyakorlat.stream().map(
                        csoportWithGyakorlatTerv -> csoportWithGyakorlatTerv.csoportEntity.getIzomcsoport()
                ).distinct().collect(Collectors.toList());
                Edzesnap edzesnap = new Edzesnap(eddzesnap.edzesnapEntity.getEdzesNapLabel());
                csoport.forEach(s -> {
                    Csoport csoport1 = new Csoport(s);
                    for (CsoportWithGyakorlatTerv csoportsWithGyakorlat : eddzesnap.csoportsWithGyakorlat) {
                        if (csoportsWithGyakorlat.csoportEntity.getIzomcsoport().equals(s)) {
                            for (GyakorlatTervEntity gy : csoportsWithGyakorlat.gyakorlatTervEntities) {
                                csoport1.addGyakorlat(tervModelConverter.getFromEntity(gy));
                            }
                        }
                    }
                    edzesnap.addCsoport(csoport1);
                });
                edzesTerv.addEdzesNap(edzesnap);
            }
            edzesTervs.add(edzesTerv);
        }

        return edzesTervs;
    }
}
