package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListUtil;

public class ListItemViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "ListItemViewFactory";
    private NaploEsNaplo naploEsNaplo;
    private Context context;
    private int appWidgetId;
    private SimpleDateFormat simpleDateFormat;
    private NaploRepository naploRepository;
    private ModelConverter modelConverter;

    public ListItemViewFactory(Context context, Intent intent, NaploRepository naploRepository,
                               ModelConverter modelConverter) {
        this.context = context;
        this.modelConverter = modelConverter;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.naploRepository = naploRepository;
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Override
    public void onCreate() {
        naploEsNaplo = getNaploGyakOsszsuly();
    }

    @Override
    public void onDataSetChanged() {
        naploEsNaplo = getNaploGyakOsszsuly();
    }

    @Override
    public void onDestroy() {
        naploEsNaplo = null;
    }

    @Override
    public int getCount() {
        return naploEsNaplo.getNaploGyakOsszsulyList().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        NaploGyakOsszsuly naploGyakOsszsuly = naploEsNaplo.getNaploGyakOsszsulyList().get(position);

        views.setTextViewText(R.id.tvNaploDatum, simpleDateFormat.format(new Date(naploGyakOsszsuly.getNaplodatum())));
        views.setTextViewText(R.id.tvGyakOssz,
                String.format(Locale.getDefault(), "%,d Kg", naploGyakOsszsuly.getGyakorlatOsszsulys()));
        views.setTextViewText(R.id.tvIzomcsop, naploEsNaplo.getIzomcsoportLista(naploEsNaplo.getNaploWithSorozatList().get(position)));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(NaploDetailsActivity.EXTRA_NAPLO, naploGyakOsszsuly.getNaploUI());
        views.setOnClickFillInIntent(R.id.tvNaploDatum, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private NaploEsNaplo getNaploGyakOsszsuly() {
        List<NaploGyakOsszsuly> naploGyakOsszsulies = new ArrayList<>();

        List<NaploWithSorozat> naploWithSorozats = naploRepository.getNaploWithSorozatList();

        if (naploWithSorozats != null) {
            for (NaploWithSorozat sorozat : naploWithSorozats) {
                naploGyakOsszsulies.add(new NaploGyakOsszsuly(sorozat.daonaplo.getNaplodatum(),
                        sorozat.sorozats.stream().mapToInt(sor ->sor.sorozat.getIsmetles() * sor.sorozat.getSuly()).sum(),
                        modelConverter.fromNaploEntity(sorozat)));
            }
        }
        return new NaploEsNaplo(naploGyakOsszsulies, naploWithSorozats);
    }

    private static class NaploEsNaplo {
        final List<NaploGyakOsszsuly> naploGyakOsszsulyList;
        final List<NaploWithSorozat> naploWithSorozatList;

        public NaploEsNaplo(List<NaploGyakOsszsuly> naploGyakOsszsulyList, List<NaploWithSorozat> naploWithSorozatList) {
            this.naploGyakOsszsulyList = naploGyakOsszsulyList;
            this.naploWithSorozatList = naploWithSorozatList;
        }

        public List<NaploGyakOsszsuly> getNaploGyakOsszsulyList() {
            return naploGyakOsszsulyList;
        }

        public List<NaploWithSorozat> getNaploWithSorozatList() {
            return naploWithSorozatList;
        }

        public String getIzomcsoportLista(NaploWithSorozat naploWithSorozats) {
            Set<String> izomcsoportok = new LinkedHashSet<>();
            for (SorozatWithGyakorlat sorozat : naploWithSorozats.sorozats) {
                Gyakorlat gyakorlat = sorozat.gyakorlat;
                if(gyakorlat != null)
                    izomcsoportok.add(gyakorlat.getCsoport());
                else
                    izomcsoportok.add("...");

            }
            StringBuilder sb = new StringBuilder();
            izomcsoportok.forEach(csoport -> sb.append(csoport).append(", "));
            return izomcsoportok.size() > 0 ? sb.toString().substring(0, sb.toString().lastIndexOf(',')) : "";
        }

        public int getNaploOsszSuly(NaploWithSorozat naploWithSorozat) {
            int ossz = 0;
            for (SorozatWithGyakorlat sorozat: naploWithSorozat.sorozats) {
                ossz += sorozat.sorozat.getSuly() * sorozat.sorozat.getIsmetles();
            }
            return ossz;
        }

        public String getGyakorlatLista(NaploWithSorozat naploWithSorozat) {
            Set<String> gyakorlatok = new LinkedHashSet<>();
            for (SorozatWithGyakorlat sorozat: naploWithSorozat.sorozats)
                gyakorlatok.add(sorozat.gyakorlat.getMegnevezes());

            StringBuilder sb = new StringBuilder();
            gyakorlatok.forEach(csoport -> sb.append(csoport).append(", "));
            return gyakorlatok.size() > 0 ? sb.toString().substring(0, sb.toString().lastIndexOf(',')) : "";
        }
    }
}
