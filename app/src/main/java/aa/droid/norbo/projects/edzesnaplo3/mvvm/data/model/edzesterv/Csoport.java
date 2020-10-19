package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Csoport implements Serializable {
    private String izomcsoport;
    private List<GyakorlatTerv> valasztottGyakorlatok;

    public Csoport() {
    }

    public Csoport(String izomcsoport) {
        this.izomcsoport = izomcsoport;
        this.valasztottGyakorlatok = new ArrayList<>();
    }

    public void addGyakorlat(GyakorlatTerv gyakorlatTerv) {
        valasztottGyakorlatok.add(gyakorlatTerv);
    }

    public void setIzomcsoport(String izomcsoport) {
        this.izomcsoport = izomcsoport;
    }

    public void setValasztottGyakorlatok(List<GyakorlatTerv> valasztottGyakorlatok) {
        this.valasztottGyakorlatok = valasztottGyakorlatok;
    }

    public List<GyakorlatTerv> getValasztottGyakorlatok() {
        return valasztottGyakorlatok;
    }

    public String getIzomcsoport() {
        return izomcsoport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Csoport csoport = (Csoport) o;
        return Objects.equals(izomcsoport, csoport.izomcsoport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(izomcsoport);
    }

    @Override
    public String toString() {
        return izomcsoport+" ";
    }
}
