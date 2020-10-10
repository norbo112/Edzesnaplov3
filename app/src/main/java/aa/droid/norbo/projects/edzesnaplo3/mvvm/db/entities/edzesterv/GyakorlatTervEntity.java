package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class GyakorlatTervEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int csoportId;
    private int edzesnapId;
    private int edzesTervId;
    private String megnevezes;
    private List<Integer> sorozatSzam;
    private List<Integer> ismetlesSzam;

    public GyakorlatTervEntity() {
    }

    @Ignore
    public GyakorlatTervEntity(int edzesTervId, int edzesnapId, int csoportId, String megnevezes, List<Integer> sorozatSzam, List<Integer> ismetlesSzam) {
        this.edzesTervId = edzesTervId;
        this.edzesnapId = edzesnapId;
        this.csoportId = csoportId;
        this.megnevezes = megnevezes;
        this.sorozatSzam = sorozatSzam;
        this.ismetlesSzam = ismetlesSzam;
    }

    public int getEdzesTervId() {
        return edzesTervId;
    }

    public void setEdzesTervId(int edzesTervId) {
        this.edzesTervId = edzesTervId;
    }

    public int getEdzesnapId() {
        return edzesnapId;
    }

    public void setEdzesnapId(int edzesnapId) {
        this.edzesnapId = edzesnapId;
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

    public int getCsoportId() {
        return csoportId;
    }

    public void setCsoportId(int csoportId) {
        this.csoportId = csoportId;
    }

    public List<Integer> getSorozatSzam() {
        return sorozatSzam;
    }

    public void setSorozatSzam(List<Integer> sorozatSzam) {
        this.sorozatSzam = sorozatSzam;
    }

    public List<Integer> getIsmetlesSzam() {
        return ismetlesSzam;
    }

    public void setIsmetlesSzam(List<Integer> ismetlesSzam) {
        this.ismetlesSzam = ismetlesSzam;
    }
}
