package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

@Entity(tableName = "sorozattabla")
public class Sorozat implements Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int gyakorlatid;
    private int suly;
    private int ismetles;
    private long ismidopont;
    private String naplodatum;
    @Ignore
    private Gyakorlat gyakorlat;

    public Sorozat(){}

    @Ignore
    public Sorozat(Sorozat sorozat) {
        this.gyakorlatid = sorozat.gyakorlatid;
        this.suly = sorozat.suly;
        this.ismetles = sorozat.ismetles;
        this.ismidopont = sorozat.ismidopont;
        this.naplodatum = sorozat.naplodatum;
    }

    @Ignore
    private Sorozat(Parcel parcel) {
        id = parcel.readInt();
        gyakorlatid = parcel.readInt();
        suly = parcel.readInt();
        ismetles = parcel.readInt();
        ismidopont = parcel.readLong();
        naplodatum = parcel.readString();
        gyakorlat = (Gyakorlat) parcel.readSerializable();
    }

    @Ignore
    public Sorozat(Gyakorlat gyakorlat, int suly, int ismetles, long ismidopont, String naplodatum) {
        this.gyakorlat = gyakorlat;
        this.gyakorlatid = gyakorlat.getId();
        this.suly = suly;
        this.ismetles = ismetles;
        this.ismidopont = ismidopont;
        this.naplodatum = naplodatum;
    }

    public static final Creator<Sorozat> CREATOR = new Creator<Sorozat>() {
        @Override
        public Sorozat createFromParcel(Parcel in) {
            return new Sorozat(in);
        }

        @Override
        public Sorozat[] newArray(int size) {
            return new Sorozat[size];
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

    public int getSuly() {
        return suly;
    }

    public void setSuly(int suly) {
        this.suly = suly;
    }

    public int getIsmetles() {
        return ismetles;
    }

    public void setIsmetles(int ismetles) {
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
        Sorozat sorozat = (Sorozat) o;
        return id == sorozat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "["+gyakorlatid+"] "+suly+"X"+ismetles+" "+ DateTimeFormatter.getTime(ismidopont) +" "+(suly*ismetles)+" Kg\n";
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
        dest.writeInt(suly);
        dest.writeInt(ismetles);
        dest.writeLong(ismidopont);
        dest.writeString(naplodatum);
        dest.writeSerializable(gyakorlat);
    }
}
