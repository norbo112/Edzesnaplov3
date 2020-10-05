package aa.droid.norbo.projects.edzesnaplo3.widgets;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NaploGyakOsszsuly implements Serializable {
    private String naplodatum;
    private List<String> gyakorlats;
    private List<String> izomcsoportok;
    private List<Integer> gyakorlatOsszsulys;
    private final String[] honapok = new String[] {
            "Jan", "Feb","Mar","Apr","Maj","Jun","Jul","Aug","Sep","Okt","Nov","Dec"
    };

    public NaploGyakOsszsuly(String naplo,List<String> izomcsoportok, List<String> gyakorlats, List<Integer> gyakorlatOsszsulys) {
        this.naplodatum = naplo;
        this.gyakorlats = gyakorlats;
        this.gyakorlatOsszsulys = gyakorlatOsszsulys;
        this.izomcsoportok = izomcsoportok;
    }

    public String getNaplodatum() {
        return naplodatum;
    }

    public List<String> getGyakorlats() {
        return gyakorlats;
    }

    public List<Integer> getGyakorlatOsszsulys() {
        return gyakorlatOsszsulys;
    }

    public List<String> getIzomcsoportok() {
        return izomcsoportok;
    }

    @NonNull
    @Override
    public String toString() {
        Date date = new Date(naplodatum);
        return date.getYear()+"."+date.getMonth()+"."+date.getDay()+" ";
    }
}
