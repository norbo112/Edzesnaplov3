package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GyakorlatTerv {
    private String megnevezes;
    private int id;
    private List<Integer> sorozatSzam;
    private List<Integer> ismetlesSzam;

    public GyakorlatTerv(String megnevezes, int id) {
        this.megnevezes = megnevezes;
        this.id = id;
        this.sorozatSzam = new ArrayList<>();
        this.ismetlesSzam = new ArrayList<>();
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addSorozatSzam(int sorozatSzam) {
        this.sorozatSzam.add(sorozatSzam);
    }

    public List<Integer> getSorozatSzam() {
        return sorozatSzam;
    }

    public void addIsmetlesSzam(int ismetlesSzam) {
        this.ismetlesSzam.add(ismetlesSzam);
    }

    public List<Integer> getIsmetlesSzam() {
        return ismetlesSzam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GyakorlatTerv that = (GyakorlatTerv) o;
        return id == that.id &&
                megnevezes.equals(that.megnevezes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(megnevezes, id);
    }

    @Override
    public String toString() {
        return megnevezes+" sor: "+sorozatSzam.toString()+", ism: "+ismetlesSzam.toString();
    }
}
