package aa.droid.norbo.projects.edzesnaplo3.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "sorozattabla", foreignKeys = {
        @ForeignKey(entity = Naplo.class,
            parentColumns = "id",
            childColumns = "naploid")
}, indices = {@Index(value = "naploid")})
public class Sorozat {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int gyakorlatid;
    private int suly;
    private int ismetles;
    private String ismidopont;
    private int naploid;

    public Sorozat(){}

    @Ignore
    public Sorozat(int gyakorlatid, int suly, int ismetles, String ismidopont, int naploid) {
        this.gyakorlatid = gyakorlatid;
        this.suly = suly;
        this.ismetles = ismetles;
        this.ismidopont = ismidopont;
        this.naploid = naploid;
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

    public int getNaploid() {
        return naploid;
    }

    public void setNaploid(int naploid) {
        this.naploid = naploid;
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
}
