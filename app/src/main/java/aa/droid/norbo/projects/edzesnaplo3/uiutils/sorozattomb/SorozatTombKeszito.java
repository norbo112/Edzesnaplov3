package aa.droid.norbo.projects.edzesnaplo3.uiutils.sorozattomb;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.lists.SorozatSorter;

public class SorozatTombKeszito {

    public String[] getIsmIdoStrLongFromSorozats(List<Sorozat> sorozatList) {
        if(sorozatList == null) return new String[0];

        //itt feltételezem hogy longba vannak megadva az időpontok, és igy sorbarendezem
        SorozatSorter.rcSorozatSorter(sorozatList);

        String[] result = new String[sorozatList.size()];
        for (int i = 0; i < sorozatList.size(); i++) {
            result[i] = sorozatList.get(i).getIsmidopont();
        }
        return result;
    }

    /**
     * long minutes = (milliseconds / 1000) / 60;
     * Sorbarendezett időpontokból kiszámítja az eltelt perceket
     * @param idopontok minimum két érték
     * @return
     */
    public long getEltelIdoSzamitas(String[] idopontok) {
        if(idopontok != null && idopontok.length < 2) throw new IllegalArgumentException("Üres tömb nem lehet, minimum 2 elem");

        if(idopontok == null) throw new IllegalArgumentException("Nem lehet null a tömb");

        long a = Long.parseLong(idopontok[0]);
        long b = Long.parseLong(idopontok[idopontok.length-1]);

        return Math.abs(((a - b)/1000)/60);
    }
}
