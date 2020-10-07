package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class EdzesnapEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int edzestervId;
    private String edzesNapLabel; //1.nap, 2.nap, 3.nap

    public EdzesnapEntity() {
    }

    @Ignore
    public EdzesnapEntity(int edzestervId, String edzesNapLabel) {
        this.edzestervId = edzestervId;
        this.edzesNapLabel = edzesNapLabel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdzesNapLabel() {
        return edzesNapLabel;
    }

    public void setEdzesNapLabel(String edzesNapLabel) {
        this.edzesNapLabel = edzesNapLabel;
    }

    public int getEdzestervId() {
        return edzestervId;
    }

    public void setEdzestervId(int edzestervId) {
        this.edzestervId = edzestervId;
    }
}
