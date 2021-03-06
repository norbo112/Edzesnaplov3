package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GyakorlatTerv implements Serializable {
    private String megnevezes;
    private String izomcsoportNev;
    private List<Integer> sorozatSzam;
    private List<Integer> ismetlesSzam;

    public GyakorlatTerv() {
    }

    public GyakorlatTerv(String megnevezes, String izomcsoportNev) {
        this.megnevezes = megnevezes;
        this.izomcsoportNev = izomcsoportNev;
        this.sorozatSzam = new ArrayList<>();
        this.ismetlesSzam = new ArrayList<>();
    }

    public GyakorlatTerv(String megnevezes, List<Integer> sorozatSzam, List<Integer> ismetlesSzam) {
        this.megnevezes = megnevezes;
        this.sorozatSzam = sorozatSzam;
        this.ismetlesSzam = ismetlesSzam;
    }

    public void setSorozatSzam(List<Integer> sorozatSzam) {
        this.sorozatSzam = sorozatSzam;
    }

    public void setIsmetlesSzam(List<Integer> ismetlesSzam) {
        this.ismetlesSzam = ismetlesSzam;
    }

    public String getIzomcsoportNev() {
        return izomcsoportNev;
    }

    public void setIzomcsoportNev(String izomcsoportNev) {
        this.izomcsoportNev = izomcsoportNev;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
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
        return megnevezes.equals(that.megnevezes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(megnevezes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<sorozatSzam.size(); i++) {
            sb.append(sorozatSzam.get(i)).append("x").append(ismetlesSzam.get(i)).append(", ");
        }
        return megnevezes+ (sorozatSzam.size() != 0 ?
                " sor: "+sb.toString().substring(0, sb.toString().lastIndexOf(",")) : " 0x0");
    }
}
