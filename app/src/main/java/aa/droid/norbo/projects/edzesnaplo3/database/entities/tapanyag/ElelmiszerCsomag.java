package aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag;

import java.util.List;

public class ElelmiszerCsomag {
    private String nev;
    private List<Elelmiszer> tapanyagok;

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public List<Elelmiszer> getTapanyagok() {
        return tapanyagok;
    }

    public void setTapanyagok(List<Elelmiszer> tapanyagok) {
        this.tapanyagok = tapanyagok;
    }
}
