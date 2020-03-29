package aa.droid.norbo.projects.edzesnaplo3.datainterfaces;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

public interface AdatBeallitoInterface {
    void adatGyakorlat(Gyakorlat gyakorlat);
    void adatNaplo(Naplo naplo);
    String getFelhasznaloNev();
}
