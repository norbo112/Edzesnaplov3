package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.SorozatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;

@Singleton
public class ModelConverter {
    @Inject
    public ModelConverter() {
    }

    public Gyakorlat fromUI(GyakorlatUI gyakorlatUI) {
        return new Gyakorlat(gyakorlatUI.getId(), gyakorlatUI.getCsoport(), gyakorlatUI.getMegnevezes(),
                gyakorlatUI.getLeiras(), gyakorlatUI.getVideolink(),
                gyakorlatUI.getVideostartpoz() == null ? 0 : Integer.parseInt(gyakorlatUI.getVideostartpoz()));
    }

    public GyakorlatUI fromEntity(Gyakorlat gyakorlatEntity) {
        return new GyakorlatUI(gyakorlatEntity.getId(), gyakorlatEntity.getCsoport(), gyakorlatEntity.getMegnevezes(),
                gyakorlatEntity.getLeiras(), gyakorlatEntity.getVideolink(), Integer.toString(gyakorlatEntity.getVideostartpoz()));
    }

    public SorozatUI fromSorozatEntity(Sorozat sorozat) {
        return new SorozatUI(sorozat.getGyakorlat(),
                Integer.toString(sorozat.getSuly()),
                Integer.toString(sorozat.getIsmetles()), sorozat.getIsmidopont(), sorozat.getNaplodatum());
    }

    public Sorozat fromSorozatUI(SorozatUI sorozatUI) {
        return new Sorozat(sorozatUI.getGyakorlat(),
                Integer.parseInt(sorozatUI.getSuly()),
                Integer.parseInt(sorozatUI.getIsmetles()), sorozatUI.getIsmidopont(),
                sorozatUI.getNaplodatum());
    }
}
