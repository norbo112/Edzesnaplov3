package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Csoport {
    private String izomcsoport;
    private List<GyakorlatTerv> valasztottGyakorlatok;

    public Csoport(String izomcsoport) {
        this.izomcsoport = izomcsoport;
        this.valasztottGyakorlatok = new ArrayList<>();
    }

    public void addGyakorlat(GyakorlatTerv gyakorlatTerv) {
        valasztottGyakorlatok.add(gyakorlatTerv);
    }

    public List<GyakorlatTerv> getValasztottGyakorlatok() {
        return valasztottGyakorlatok;
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
}