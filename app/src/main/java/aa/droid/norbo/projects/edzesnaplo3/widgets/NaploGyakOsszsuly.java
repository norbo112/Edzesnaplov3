package aa.droid.norbo.projects.edzesnaplo3.widgets;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NaploGyakOsszsuly {
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
        String[] splittedNaploDatum = naplodatum.split(" ");
        int poz = -1;
        for (int i = 0; i < honapok.length; i++) {
            if(honapok[i].equals(splittedNaploDatum[1])) {
                poz = i + 1;
                break;
            }
        }
        String pozstr = (poz<10)  ? "0"+poz : poz+"";
        return splittedNaploDatum[5]+"."+pozstr+"."+splittedNaploDatum[2];
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
