package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.model;

public class GyakorlatSorozatElteltIdo {
    private long naploDatum;
    private int elteltIdo;

    public GyakorlatSorozatElteltIdo(long naploDatum, int elteltIdo) {
        this.naploDatum = naploDatum;
        this.elteltIdo = elteltIdo;
    }

    public long getNaploDatum() {
        return naploDatum;
    }

    public void setNaploDatum(long naploDatum) {
        this.naploDatum = naploDatum;
    }

    public int getElteltIdo() {
        return elteltIdo;
    }

    public void setElteltIdo(int elteltIdo) {
        this.elteltIdo = elteltIdo;
    }
}
