package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.NaploUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.SorozatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
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

    public NaploUI fromNaploEntity(NaploWithSorozat naploWithSorozat) {
        Naplo naplo = naploWithSorozat.daonaplo;
        NaploUI naploUI = new NaploUI(naplo.getId(), naplo.getNaplodatum(), naplo.getFelhasznalonev(), naplo.getCommentFilePath());
        naploUI.setSorozats(naploWithSorozat.sorozats);
        return naploUI;
    }

    public Naplo fromNaploUI(NaploUI naploUI) {
        return new Naplo(naploUI.getId(), naploUI.getNaplodatum(), naploUI.getFelhasznalonev(), naploUI.getCommentFilePath());
    }

    /**
     * V3-as naplók mentésének betöltésére szolgál
     * @param naplo
     * @return
     */
    public Naplo getNaploFromV3(aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Naplo naplo) {
        return new Naplo(Long.parseLong(naplo.getNaplodatum()), "kulso_forras", naplo.getCommentFilePath());
    }

    /**
     * V3-as naplók mentésének betöltésére szolgál
     * @param sorozat
     * @return
     */
    public Sorozat getSorozatFromV3(aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.Sorozat sorozat) {
        return new Sorozat(sorozat.getGyakorlatid(), sorozat.getSuly(), sorozat.getIsmetles(),
                Long.parseLong(sorozat.getIsmidopont()),
                Long.parseLong(sorozat.getNaplodatum()));
    }

    /**
     * A készülékkel mentett naplók megosztása más készülékekkel, ilyenkor új id kell,
     * tehát, új napló lesz beszúrva
     * @param naplo
     * @return
     */
    public Naplo getNewNaplo(Naplo naplo) {
        return new Naplo(naplo.getNaplodatum(), naplo.getFelhasznalonev(),
                naplo.getCommentFilePath());
    }

    /**
     * Készülékek közt megosztott napló sorozatai, újként lesznek beszúrva, id-k ütközése miatt
     * @param sorozat
     * @return
     */
    public Sorozat getNewSorozat(Sorozat sorozat) {
        return new Sorozat(sorozat.getGyakorlatid(), sorozat.getSuly(), sorozat.getIsmetles(), sorozat.getIsmidopont(),
                sorozat.getNaplodatum());
    }
}
