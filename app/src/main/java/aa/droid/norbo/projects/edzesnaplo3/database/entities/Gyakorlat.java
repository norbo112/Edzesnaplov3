package aa.droid.norbo.projects.edzesnaplo3.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "gyakorlattabla")
public class Gyakorlat implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gyakorlat gyakorlat = (Gyakorlat) o;
        return megnevezes.equals(gyakorlat.megnevezes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(megnevezes);
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "["+csoport+"] "+megnevezes;
    }
}
