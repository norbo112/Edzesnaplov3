package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "naplouser")
public class NaploUser implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String felhasznalonev;

    public NaploUser() {
    }

    public int getId() {
        return id;
    }

    public String getFelhasznalonev() {
        return felhasznalonev;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFelhasznalonev(String felhasznalonev) {
        this.felhasznalonev = felhasznalonev;
    }
}
