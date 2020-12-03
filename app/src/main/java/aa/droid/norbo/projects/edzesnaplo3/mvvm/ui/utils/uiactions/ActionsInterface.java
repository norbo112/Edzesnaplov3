package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions;

import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.SorozatDisplay;

public interface ActionsInterface {
    SwitchCompat getSwitchCompat();
    SorozatDisplay getSorozatUI();
    GyakorlatUI getGyakorlatUI();
    TextView getEtSulyView();
    TextView getEtIsmView();
}
