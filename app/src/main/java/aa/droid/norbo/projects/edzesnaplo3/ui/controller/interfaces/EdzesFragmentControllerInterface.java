package aa.droid.norbo.projects.edzesnaplo3.ui.controller.interfaces;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

/**
 * Edzés fragment Gyakorlat felvételekor,  gombok és szövegek megváltoztatása
 */
public interface EdzesFragmentControllerInterface {
    /**
     * Fragmentbe lévő view modosítása és a fragmenthez való gyakorlat felvétel
     * @param view
     */
    void prepareGyakorlat(Fragment fragment, View view);

    void disableButtons(Fragment fragment, View view);

    void enableButtons(Fragment fragment, View view);
}
