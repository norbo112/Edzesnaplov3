package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CsoportEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int edzesNapId;
    private int edzesTervId;
    private String izomcsoport;

    public CsoportEntity() {
    }

    @Ignore
    public CsoportEntity(String izomcsoport, int edzesNapId, int edzesTervId) {
        this.izomcsoport = izomcsoport;
        this.edzesNapId = edzesNapId;
        this.edzesTervId = edzesTervId;
    }

    public int getEdzesTervId() {
        return edzesTervId;
    }

    public void setEdzesTervId(int edzesTervId) {
        this.edzesTervId = edzesTervId;
    }

    public int getEdzesNapId() {
        return edzesNapId;
    }

    public void setEdzesNapId(int edzesNapId) {
        this.edzesNapId = edzesNapId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzomcsoport() {
        return izomcsoport;
    }

    public void setIzomcsoport(String izomcsoport) {
        this.izomcsoport = izomcsoport;
    }
}
