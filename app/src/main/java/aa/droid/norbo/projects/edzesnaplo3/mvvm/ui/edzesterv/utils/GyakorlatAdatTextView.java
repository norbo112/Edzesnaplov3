package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;

public class GyakorlatAdatTextView extends AppCompatTextView {
    private Gyakorlat gyakorlat;

    public GyakorlatAdatTextView(@NonNull Context context, Gyakorlat gyakorlat) {
        super(context);
        this.gyakorlat = gyakorlat;
    }

    public Gyakorlat getGyakorlat() {
        return gyakorlat;
    }
}
