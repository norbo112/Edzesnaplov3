package aa.droid.norbo.projects.edzesnaplo3.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "naplo")
public class Naplo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String naplodatum;
    private String felhasznalonev;
    @Ignore
    private List<Sorozat> sorozats;

    public Naplo(){}

    @Ignore
    public Naplo(String naplodatum, String felhasznalonev) {
        this.naplodatum = naplodatum;
        this.felhasznalonev = felhasznalonev;
        this.sorozats = new ArrayList<>();
    }

    public void addSorozat(Sorozat sorozat) {
        sorozats.add(sorozat);
    }

    public boolean removeSorozat(Sorozat sorozat) {
        return sorozats.remove(sorozat);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaplodatum() {
        return naplodatum;
    }

    public void setNaplodatum(String naplodatum) {
        this.naplodatum = naplodatum;
    }

    public String getFelhasznalonev() {
        return felhasznalonev;
    }

    public void setFelhasznalonev(String felhasznalonev) {
        this.felhasznalonev = felhasznalonev;
    }

    @Ignore
    public List<Sorozat> getSorozats() {
        return sorozats;
    }

    @Ignore
    public void setSorozats(List<Sorozat> sorozats) {
        this.sorozats = sorozats;
    }
}
