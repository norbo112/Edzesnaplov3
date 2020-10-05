package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatSorozatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class NaploDetailsRcViewAdapterFactory {
    private Context context;

    @Inject
    public NaploDetailsRcViewAdapterFactory(@ActivityContext Context context) {
        this.context = context;
    }

    public NaploDetailsRcViewAdapter create(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        return new NaploDetailsRcViewAdapter(makeGyakorlatSorozat(sorozatWithGyakorlats), context);
    }

    private List<GyakorlatSorozatUI> makeGyakorlatSorozat(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        List<GyakorlatSorozatUI> list = new ArrayList<>();

        Set<Gyakorlat> gyakorlats = sorozatWithGyakorlats.stream().map(sor -> sor.gyakorlat).collect(Collectors.toSet());
        for (Gyakorlat gyakorlat: gyakorlats) {
            GyakorlatSorozatUI gyakorlatSorozatUI = new GyakorlatSorozatUI(gyakorlat);
            for (SorozatWithGyakorlat sorozatWithGyakorlat: sorozatWithGyakorlats) {
                if(sorozatWithGyakorlat.gyakorlat.equals(gyakorlat))
                    gyakorlatSorozatUI.addSorozat(sorozatWithGyakorlat.sorozat);
            }
            list.add(gyakorlatSorozatUI);
        }

        list.sort((o1, o2) -> Long.compare(o1.getSorozats().get(0).getIsmidopont(), o2.getSorozats().get(o2.getSorozats().size() -1).getIsmidopont()));

        return list;
    }
}
