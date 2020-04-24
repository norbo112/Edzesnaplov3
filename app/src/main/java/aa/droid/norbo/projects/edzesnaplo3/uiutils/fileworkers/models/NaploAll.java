package aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models;

import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

public class NaploAll {
    private List<SorozatWithGyakorlat> sorozatWithGyakorlats;
    private Naplo naplo;

    public NaploAll() {
    }

    public NaploAll(List<SorozatWithGyakorlat> sorozatWithGyakorlats, Naplo naplo) {
        this.sorozatWithGyakorlats = sorozatWithGyakorlats;
        this.naplo = naplo;
    }

    public List<SorozatWithGyakorlat> getSorozatWithGyakorlats() {
        return sorozatWithGyakorlats;
    }

    public void setSorozatWithGyakorlats(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        this.sorozatWithGyakorlats = sorozatWithGyakorlats;
    }

    public Naplo getNaplo() {
        return naplo;
    }

    public void setNaplo(Naplo naplo) {
        this.naplo = naplo;
    }
}
