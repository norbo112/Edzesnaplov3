package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class MentettAdatok {
    public static int getOsszNaploSuly(List<NaploWithSorozat> naplos) {
        int result = 0;
        for (int i = 0; i < naplos.size(); i++) {
            NaploWithSorozat naploWithSorozat = naplos.get(i);
            for (int j = 0; j < naploWithSorozat.sorozats.size(); j++) {
                SorozatWithGyakorlat ss = naploWithSorozat.sorozats.get(j);
                result += getSorozatOsszSuyl(ss.sorozat);
            }
        }

        return result;
    }

    private static int getSorozatOsszSuyl(Sorozat sorozat) {
        return sorozat.getSuly() * sorozat.getIsmetles();
    }
}
