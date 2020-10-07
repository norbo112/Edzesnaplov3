package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "gyakorlattabla")
public class Gyakorlat implements Serializable, Comparable<Gyakorlat> {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "csoport")
    private String csoport;
    @ColumnInfo(name = "megnevezes")
    private String megnevezes;
    @ColumnInfo(name = "leiras")
    private String leiras;
    @ColumnInfo(name = "videolink")
    private String videolink;
    @ColumnInfo(name = "videostartpoz")
    private Integer videostartpoz;

    public Gyakorlat(){}

    @Ignore
    public Gyakorlat(Integer id, String csoport, String megnevezes, String leiras, String videolink, int videostartpoz) {
        this.id = id;
        this.csoport = csoport;
        this.megnevezes = megnevezes;
        this.leiras = leiras;
        this.videolink = videolink;
        this.videostartpoz = videostartpoz;
    }

    @Ignore
    public Gyakorlat(String csoport, String megnevezes, String leiras, String videolink, int videostartpoz) {
        this.csoport = csoport;
        this.megnevezes = megnevezes;
        this.leiras = leiras;
        this.videolink = videolink;
        this.videostartpoz = videostartpoz;
    }

    @Ignore
    public Gyakorlat(int minuszid, String megnevezes) {
        this.id = minuszid;
        this.megnevezes = megnevezes;
    }

    public Integer getId() {
        return id;
    }

    public String getCsoport() {
        return csoport;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public String getLeiras() {
        return leiras;
    }

    public String getVideolink() {
        return videolink;
    }

    public Integer getVideostartpoz() {
        return videostartpoz;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCsoport(String csoport) {
        this.csoport = csoport;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public void setVideostartpoz(Integer videostartpoz) {
        this.videostartpoz = videostartpoz;
    }

    @Ignore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gyakorlat gyakorlat = (Gyakorlat) o;
        return id.equals(gyakorlat.id);
    }

    @Ignore
    @Override
    public int compareTo(Gyakorlat o) {
        return Integer.compare(this.id, o.id);
    }

    @Ignore
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return megnevezes;
    }
}
