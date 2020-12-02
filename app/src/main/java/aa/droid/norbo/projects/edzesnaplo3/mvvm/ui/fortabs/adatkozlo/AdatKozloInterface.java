package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;

/**
 * Adatokat közöl a két fragment között
 */
public interface AdatKozloInterface {
    void gyakorlatAtado(GyakorlatUI gyakorlatUI);

    /**
     * Plusz gyakorlat felvétele a szuperszetthez vagy triszett  esetleg óriás szett témához
     * @param gyakorlatUI
     */
    void pluszGyakorlatFelvetele(GyakorlatUI gyakorlatUI);
}
