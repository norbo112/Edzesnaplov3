package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels;

import java.io.Serializable;

public class OsszSorozat implements Serializable {
    private int osszism;
    private int osszsuly;
    private long naplodatum;

    public OsszSorozat() {
    }

    public int getOsszism() {
        return osszism;
    }

    public void setOsszism(int osszism) {
        this.osszism = osszism;
    }

    public int getOsszsuly() {
        return osszsuly;
    }

    public void setOsszsuly(int osszsuly) {
        this.osszsuly = osszsuly;
    }

    public long getNaplodatum() {
        return naplodatum;
    }

    public void setNaplodatum(long naplodatum) {
        this.naplodatum = naplodatum;
    }
}
