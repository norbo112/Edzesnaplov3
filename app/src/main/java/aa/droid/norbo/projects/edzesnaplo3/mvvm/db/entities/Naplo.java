package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

@Entity(tableName = "naplo")
public class Naplo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(index = true)
    private long naplodatum;
    private String felhasznalonev;
    @ColumnInfo(name = "sound_comment_fname")
    private String commentFilePath;
    @Ignore
    private List<Sorozat> sorozats;

    public Naplo(){}

    @Ignore
    public Naplo(@NonNull long naplodatum, String felhasznalonev) {
        this.naplodatum = naplodatum;
        this.felhasznalonev = felhasznalonev;
        this.sorozats = new ArrayList<>();
    }

    @Ignore
    public Naplo(long naplodatum, String felhasznalonev, String commentFilePath) {
        this.naplodatum = naplodatum;
        this.felhasznalonev = felhasznalonev;
        this.commentFilePath = commentFilePath;
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

    public long getNaplodatum() {
        return naplodatum;
    }

    public void setNaplodatum(long naplodatum) {
        this.naplodatum = naplodatum;
    }

    public String getFelhasznalonev() {
        return felhasznalonev;
    }

    public void setFelhasznalonev(String felhasznalonev) {
        this.felhasznalonev = felhasznalonev;
    }

    public String getCommentFilePath() {
        return commentFilePath;
    }

    public void setCommentFilePath(String commentFilePath) {
        this.commentFilePath = commentFilePath;
    }

    @Ignore
    public List<Sorozat> getSorozats() {
        return sorozats;
    }

    @Ignore
    public void setSorozats(List<Sorozat> sorozats) {
        this.sorozats = sorozats;
    }

    @Ignore
    public void addAllSorozat(final List<Sorozat> sorozats) {
        this.sorozats.addAll(sorozats);
    }

    @NonNull
    @Override
    public String toString() {
//        return DateTimeFormatter.getNaploDatum(naplodatum);
        return DateTimeFormatter.getNaploDatum(naplodatum)+"\n"+sorozats;
    }
}
