package aa.droid.norbo.projects.edzesnaplo3.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatGyakorlatNevvel;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

public class SpinnerAdapters {
    private Context mContext;

    public SpinnerAdapters(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayAdapter getSpinnerAdapter(List<SorozatWithGyakorlat> sorozatok) {
        TreeSet<Gyakorlat> megnevezess = new TreeSet<>();
        megnevezess.add(new Gyakorlat(-1, "Válassz gyakorlatot..."));
        for(SorozatWithGyakorlat s: sorozatok) {
            megnevezess.add(s.gyakorlat);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, new ArrayList<>(megnevezess));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
