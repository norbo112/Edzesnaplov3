package aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.Objects;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

public class SorozatUI implements Serializable, Parcelable {
    private int id;
    private int gyakorlatid;
    private String suly;
    private String ismetles;
    private long ismidopont;
    private String naplodatum;
    private Gyakorlat gyakorlat;

    public SorozatUI(){}

    public SorozatUI(SorozatUI sorozat) {
        this.gyakorlatid = sorozat.gyakorlatid;
        this.suly = sorozat.suly;
        this.ismetles = sorozat.ismetles;
        this.ismidopont = sorozat.ismidopont;
        this.naplodatum = sorozat.naplodatum;
    }

    private SorozatUI(Parcel parcel) {
        id = parcel.readInt();
        gyakorlatid = parcel.readInt();
        suly = parcel.readString();
        ismetles = parcel.readString();
        ismidopont = parcel.readLong();
        naplodatum = parcel.readString();
        gyakorlat = (Gyakorlat) parcel.readSerializable();
    }

    public SorozatUI(Gyakorlat gyakorlat, String suly, String ismetles, long ismidopont, String naplodatum) {
        this.gyakorlat = gyakorlat;
        this.gyakorlatid = gyakorlat.getId();
        this.suly = suly;
        this.ismetles = ismetles;
        this.ismidopont = ismidopont;
        this.naplodatum = naplodatum;
    }

    public static final Creator<SorozatUI> CREATOR = new Creator<SorozatUI>() {
        @Override
        public SorozatUI createFromParcel(Parcel in) {
            return new SorozatUI(in);
        }

        @Override
        public SorozatUI[] newArray(int size) {
            return new SorozatUI[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGyakorlatid() {
        return gyakorlatid;
    }

    public void setGyakorlatid(int gyakorlatid) {
        this.gyakorlatid = gyakorlatid;
    }

    public String getSuly() {
        return suly;
    }

    public void setSuly(String suly) {
        this.suly = suly;
    }

    public String getIsmetles() {
        return ismetles;
    }

    public void setIsmetles(String ismetles) {
        this.ismetles = ismetles;
    }

    public long getIsmidopont() {
        return ismidopont;
    }

    public void setIsmidopont(long ismidopont) {
        this.ismidopont = ismidopont;
    }

    public String getNaplodatum() {
        return naplodatum;
    }

    public void setNaplodatum(String naplodatum) {
        this.naplodatum = naplodatum;
    }

    @Ignore
    public Gyakorlat getGyakorlat() {
        return gyakorlat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SorozatUI sorozat = (SorozatUI) o;
        return id == sorozat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "["+gyakorlatid+"] "+suly+"X"+ismetles+" "+ DateTimeFormatter.getTime(ismidopont) +" "+
                (Integer.parseInt(suly)*Integer.parseInt(ismetles))+" Kg\n";
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(gyakorlatid);
        dest.writeString(suly);
        dest.writeString(ismetles);
        dest.writeLong(ismidopont);
        dest.writeString(naplodatum);
        dest.writeSerializable(gyakorlat);
    }
}
