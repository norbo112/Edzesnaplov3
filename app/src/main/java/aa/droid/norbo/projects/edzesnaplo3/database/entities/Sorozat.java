package aa.droid.norbo.projects.edzesnaplo3.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "sorozattabla")
public class Sorozat implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int gyakorlatid;
    private int suly;
    private int ismetles;
    private String ismidopont;
    private String naplodatum;
    @Ignore
    private Gyakorlat gyakorlat;

    public Sorozat(){}

    @Ignore
    public Sorozat(Gyakorlat gyakorlat,  int suly, int ismetles, String ismidopont, String naplodatum) {
        this.gyakorlat = gyakorlat;
        this.gyakorlatid = gyakorlat.getId();
        this.suly = suly;
        this.ismetles = ismetles;
        this.ismidopont = ismidopont;
        this.naplodatum = naplodatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGyakorlatid() {
        return gyakorlatid;
    }

    public void setGyakorlatid(int gyakorlatid) {
        this.gyakorlatid = gyakorlatid;
    }

    public int getSuly() {
        return suly;
    }

    public void setSuly(int suly) {
        this.suly = suly;
    }

    public int getIsmetles() {
        return ismetles;
    }

    public void setIsmetles(int ismetles) {
        this.ismetles = ismetles;
    }

    public String getIsmidopont() {
        return ismidopont;
    }

    public void setIsmidopont(String ismidopont) {
        this.ismidopont = ismidopont;
    }

    public String getNaplodatum() {
        return naplodatum;
    }

    public void setNaplodatum(String naplodatum) {
        this.naplodatum = naplodatum;
    }

    @Ignore
    public Gyakorlat getGyakorlat() {
        return gyakorlat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sorozat sorozat = (Sorozat) o;
        return id == sorozat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return suly+"X"+ismetles+"::"+ismidopont+" "+(suly*ismetles)+" Kg";
    }
}
