package aa.droid.norbo.projects.edzesnaplo3.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "gyakorlattabla")
public class Gyakorlat {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String csoport;
    @ColumnInfo(index = true)
    private String megnevezes;
    private String leiras;
    private String videolink;
    private int videostartpoz;

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

    public int getVideostartpoz() {
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

    public void setVideostartpoz(int videostartpoz) {
        this.videostartpoz = videostartpoz;
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "["+csoport+"] "+megnevezes;
    }
}
