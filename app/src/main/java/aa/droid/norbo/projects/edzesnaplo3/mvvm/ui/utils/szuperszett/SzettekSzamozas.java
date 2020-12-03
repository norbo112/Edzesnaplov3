package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.szuperszett;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class SzettekSzamozas {
    private static final Character SZUPERSZETT = 'S';
    private static final Character TRISZETT = 'T';
    private static final Character ORIASSZETT = 'O'; //négy gyakorlat egyszerre

    @Inject
    public SzettekSzamozas() {
    }

    public int getTriszettSzamozas(List<String> szettek) {
        int n = 0;
        int kovetkezoSzam;
        for (int i = 0; i < szettek.size(); i++) {
            String s = szettek.get(i);
            if(!s.isEmpty() && s.charAt(0) == TRISZETT) {
                n++;
            }
        }

        if(n == 0) return 1;

        kovetkezoSzam = n / 3 + 1;
        return kovetkezoSzam;
    }

    public int getSzuperszettSzamozas(List<String> szettek) {
        int n = 0;
        int kovetkezoSzam;
        for (int i = 0; i < szettek.size(); i++) {
            String s = szettek.get(i);
            if(!s.isEmpty() && s.charAt(0) == SZUPERSZETT) {
                n++;
            }
        }

        if(n == 0) return 1;

        kovetkezoSzam = n / 2 + 1;
        return kovetkezoSzam;
    }

    public int getOriasSzamozas(List<String> szettek) {
        int n = 0;
        int kovetkezoSzam;
        for (int i = 0; i < szettek.size(); i++) {
            String s = szettek.get(i);
            if(!s.isEmpty() && s.charAt(0) == ORIASSZETT) {
                n++;
            }
        }

        if(n == 0) return 1;

        kovetkezoSzam = n / 4 + 1;
        return kovetkezoSzam;
    }
}
