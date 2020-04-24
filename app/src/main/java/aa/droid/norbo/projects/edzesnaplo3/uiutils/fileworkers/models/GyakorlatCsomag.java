package aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

public class GyakorlatCsomag {
    private List<Gyakorlat> gyakorlat;

    public GyakorlatCsomag() {
    }

    public List<Gyakorlat> getGyakorlat() {
        return gyakorlat;
    }

    public void setGyakorlat(List<Gyakorlat> gyakorlat) {
        this.gyakorlat = gyakorlat;
    }
}
