package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Edzesnap implements Serializable {
    private String edzesNapLabel; //1.nap, 2.nap, 3.nap
    private List<Csoport> valasztottCsoport;

    public Edzesnap() {
    }

    public Edzesnap(String edzesNapLabel) {
        this.edzesNapLabel = edzesNapLabel;
        this.valasztottCsoport = new ArrayList<>();
    }

    public void addCsoport(Csoport csoport) {
        valasztottCsoport.add(csoport);
    }

    public void setEdzesNapLabel(String edzesNapLabel) {
        this.edzesNapLabel = edzesNapLabel;
    }

    public void setValasztottCsoport(List<Csoport> valasztottCsoport) {
        this.valasztottCsoport = valasztottCsoport;
    }

    public void addCsoport(List<Csoport> csoportList) {
        valasztottCsoport.addAll(csoportList);
    }

    public List<Csoport> getValasztottCsoport() {
        return valasztottCsoport;
    }

    public String getEdzesNapLabel() {
        return edzesNapLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edzesnap edzesnap = (Edzesnap) o;
        return Objects.equals(edzesNapLabel, edzesnap.edzesNapLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edzesNapLabel);
    }
}
