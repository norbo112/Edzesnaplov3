package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;

public class NaploUI implements Serializable {
    private int id;
    private long naplodatum;
    private String felhasznalonev;
    private String commentFilePath;
    private List<SorozatWithGyakorlat> sorozats;

    public NaploUI(){}

    public NaploUI(int id, long naplodatum, String felhasznalonev) {
        this.id = id;
        this.naplodatum = naplodatum;
        this.felhasznalonev = felhasznalonev;
        this.sorozats = new ArrayList<>();
    }

    public NaploUI(int id, long naplodatum, String felhasznalonev, String commentFilePath) {
        this.id = id;
        this.naplodatum = naplodatum;
        this.felhasznalonev = felhasznalonev;
        this.commentFilePath = commentFilePath;
        this.sorozats = new ArrayList<>();
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

    public List<SorozatWithGyakorlat> getSorozats() {
        return sorozats;
    }

    public void setSorozats(List<SorozatWithGyakorlat> sorozats) {
        this.sorozats = sorozats;
    }

    @NonNull
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatter();
        return dateTimeFormatter.getNaploDatum(naplodatum)+ (sorozats!=null ? "\n"+sorozats : "");
    }
}
