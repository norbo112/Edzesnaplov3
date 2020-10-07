package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EdzesTerv {
    private String megnevezes;
    private List<Edzesnap> edzesnapList;

    public EdzesTerv(String megnevezes) {
        this.megnevezes = megnevezes;
        this.edzesnapList = new ArrayList<>();
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
        return "EdzesTerv{" +
                "megnevezes='" + megnevezes + '\'' +
                '}';
    }
}
