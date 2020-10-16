package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EdzesTerv implements Serializable {
    private int tervId;
    private String megnevezes;
    private List<Edzesnap> edzesnapList;

    public EdzesTerv(String megnevezes) {
        this.megnevezes = megnevezes;
        this.edzesnapList = new ArrayList<>();
    }

    public int getTervId() {
        return tervId;
    }

    public void setTervId(int tervId) {
        this.tervId = tervId;
    }

    public void addEdzesNap(Edzesnap edzesnap) {
        edzesnapList.add(edzesnap);
    }

    public List<Edzesnap> getEdzesnapList() {
        return edzesnapList;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdzesTerv edzesTerv = (EdzesTerv) o;
        return Objects.equals(megnevezes, edzesTerv.megnevezes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(megnevezes);
    }

    @Override
    public String toString() {
        if(edzesnapList != null) {
            String csoportok = edzesnapList.stream().map(edzesnap -> edzesnap.getValasztottCsoport().toString()).collect(Collectors.joining(","));
            return megnevezes+" "+csoportok;
        } else {
            return megnevezes;
        }
    }
}
