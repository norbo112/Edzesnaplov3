package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.model;

public class GyakorlatSorozatElteltIdo {
    private long naploDatum;
    private long elteltIdo;

    public GyakorlatSorozatElteltIdo(long naploDatum, long elteltIdo) {
        this.naploDatum = naploDatum;
        this.elteltIdo = elteltIdo;
    }

    public long getNaploDatum() {
        return naploDatum;
    }

    public void setNaploDatum(long naploDatum) {
        this.naploDatum = naploDatum;
    }

    public long getElteltIdo() {
        return elteltIdo;
    }

    public void setElteltIdo(long elteltIdo) {
        this.elteltIdo = elteltIdo;
    }
}
