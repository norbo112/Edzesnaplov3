package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.szuperszett;

import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.TevekenysegFragment;

public interface SzettekInterface {
    SwitchCompat getSwitchCompat();
    TevekenysegFragment.SorozatDisplay getSorozatUI();
    TextView getEtSulyView();
    TextView getEtIsmView();
}
