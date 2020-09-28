package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;

public class GyakorlatSorozatUI {
    private Gyakorlat gyakorlat;
    private List<Sorozat> sorozats;

    public GyakorlatSorozatUI(Gyakorlat gyakorlat) {
        this.gyakorlat = gyakorlat;
        this.sorozats = new ArrayList<>();
    }

    public GyakorlatSorozatUI(Gyakorlat gyakorlat, List<Sorozat> sorozats) {
        this.gyakorlat = gyakorlat;
        this.sorozats = sorozats;
    }

    public void addSorozat(Sorozat sorozat) {
        sorozats.add(sorozat);
    }

    public Gyakorlat getGyakorlat() {
        return gyakorlat;
    }

    public List<Sorozat> getSorozats() {
        return sorozats;
    }
}
