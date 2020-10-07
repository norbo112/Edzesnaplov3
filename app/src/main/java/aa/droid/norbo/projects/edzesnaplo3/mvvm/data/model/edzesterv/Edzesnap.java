package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.util.ArrayList;
import java.util.List;

public class Edzesnap {
    private String edzesNapLabel; //1.nap, 2.nap, 3.nap
    private List<Csoport> valasztottCsoport;

    public Edzesnap(String edzesNapLabel) {
        this.edzesNapLabel = edzesNapLabel;
        this.valasztottCsoport = new ArrayList<>();
    }

    public void addCsoport(Csoport csoport) {
        valasztottCsoport.add(csoport);
    }

    public List<Csoport> getValasztottCsoport() {
        return valasztottCsoport;
    }
}
