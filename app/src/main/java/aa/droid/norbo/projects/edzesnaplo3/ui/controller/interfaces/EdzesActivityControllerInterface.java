package aa.droid.norbo.projects.edzesnaplo3.ui.controller.interfaces;

import android.app.Activity;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

public interface EdzesActivityControllerInterface {
    /**
     * Ha nem fragmentbe van a téma
     * @param activity
     * @param gyakorlat
     */
    void setGyakorlat(Activity activity, Gyakorlat gyakorlat);

    void disableButtons(Activity activity);

    void enableButtons(Activity activity);
}
