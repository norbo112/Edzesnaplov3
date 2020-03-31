package aa.droid.norbo.projects.edzesnaplo3.providers.db.model;

import android.database.Cursor;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

public class NaploGyakOsszsuly {
    private Naplo naplo;
    private List<String> gyakorlats;
    private List<Integer> gyakorlatOsszsulys;

    public NaploGyakOsszsuly(Naplo naplo, List<String> gyakorlats, List<Integer> gyakorlatOsszsulys) {
        this.naplo = naplo;
        this.gyakorlats = gyakorlats;
        this.gyakorlatOsszsulys = gyakorlatOsszsulys;
    }

    public Naplo getNaplo() {
        return naplo;
    }

    public List<String> getGyakorlats() {
        return gyakorlats;
    }

    public List<Integer> getGyakorlatOsszsulys() {
        return gyakorlatOsszsulys;
    }
}
