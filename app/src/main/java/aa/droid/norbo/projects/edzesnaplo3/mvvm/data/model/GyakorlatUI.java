package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class GyakorlatUI {
    private Integer id;
    private String csoport;
    private String megnevezes;
    private String leiras;
    private String videolink;
    private String videostartpoz;

    public GyakorlatUI() {
    }

    public GyakorlatUI(String csoport, String megnevezes, String leiras, String videolink, String videostartpoz) {
        this.csoport = csoport;
        this.megnevezes = megnevezes;
        this.leiras = leiras;
        this.videolink = videolink;
        this.videostartpoz = videostartpoz;
    }

    public GyakorlatUI(Integer id, String csoport, String megnevezes, String leiras, String videolink, String videostartpoz) {
        this.id = id;
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

    public String getVideostartpoz() {
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

    public void setVideostartpoz(String videostartpoz) {
        this.videostartpoz = videostartpoz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GyakorlatUI gyakorlat = (GyakorlatUI) o;
        return id.equals(gyakorlat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return megnevezes;
    }
}
