package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProvider;
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploGyakOsszsuly;

/**
 * Az induló aktivitin lévő naplók száma és a megmozgatott súly adatok kitöltésére készítettem
 */
public class MainAdatTolto {
    private static final String TAG = "MainAdatTolto";
    private AppCompatActivity activity;
    private List<String> naploLista;
    private int naploCntint;
    private int osszSuly;

    public MainAdatTolto(AppCompatActivity pActivity) {
        this.activity = pActivity;
        initAdatok();
    }

    private void initAdatok() {
        try {
            this.naploCntint = getNaploCnt();
            this.osszSuly = getOsszSuly(naploLista);
        } catch (SQLiteException e) {
            Log.e(TAG, "initAdatok: ", e);
            this.naploCntint = 0;
            this.osszSuly = 0;
        }
    }

    private int getNaploCnt() throws SQLiteException {
        Cursor listnaplo = activity.getContentResolver().query(NaploContentProvider.GET_NAPLO, null, null,
                null, null);
        int result = 0;
        if(listnaplo != null) {
            result = listnaplo.getCount();
            naploLista = getNaploList(listnaplo);
            listnaplo.close();
        }

        return result;
    }

    private int getOsszSuly(List<String> naploLista) throws SQLiteException {
        List<NaploGyakOsszsuly> naploGyakOsszsulies = new ArrayList<>();
        List<String> gyakorlats;
        List<String> izomcsoportok;
        List<Integer> osszsuly;
        int result = 0;

        for (String naplo : naploLista) {
            gyakorlats = new ArrayList<>();
            osszsuly = new ArrayList<>();
            izomcsoportok = new ArrayList<>();
            Cursor c = activity.getContentResolver().query(NaploContentProvider.GET_GYAK_OSSZSULY, null, naplo, null, null);
            while (c.moveToNext()) {
                izomcsoportok.add(c.getString(0));
                int anInt = c.getInt(2);
                osszsuly.add(anInt);
                result += anInt;
                gyakorlats.add(c.getString(1));
            }
            naploGyakOsszsulies.add(new NaploGyakOsszsuly(naplo, izomcsoportok, gyakorlats, osszsuly));
            c.close();
        }

        return result;
    }

    private List<String> getNaploList(Cursor cursor) {
        List<String> naploList = new ArrayList<>();
        while (cursor.moveToNext()) {
            naploList.add(cursor.getString(1));
        }
        return naploList;
    }

    public int getOsszSuly() {
        return osszSuly;
    }

    public int getNaploCntint() {
        return naploCntint;
    }
}
