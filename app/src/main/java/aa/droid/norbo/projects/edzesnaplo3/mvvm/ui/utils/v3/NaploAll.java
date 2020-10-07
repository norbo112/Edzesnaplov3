package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3;

import java.util.List;

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
