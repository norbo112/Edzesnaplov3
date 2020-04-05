package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.R;

public class ListItemViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<NaploGyakOsszsuly> naploGyakOsszsulyList;
    private Context context;
    private int appWidgetId;

    public ListItemViewFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        naploGyakOsszsulyList = getNaploGyakOsszsuly(context);
    }

    @Override
    public void onDataSetChanged() {
        naploGyakOsszsulyList = getNaploGyakOsszsuly(context);
    }

    @Override
    public void onDestroy() {
        naploGyakOsszsulyList.clear();
    }

    @Override
    public int getCount() {
        return naploGyakOsszsulyList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item);
        NaploGyakOsszsuly naploGyakOsszsuly = naploGyakOsszsulyList.get(position);
        int osszsuly = 0;
        for (int i = 0; i < naploGyakOsszsuly.getGyakorlatOsszsulys().size(); i++) {
            osszsuly += naploGyakOsszsuly.getGyakorlatOsszsulys().get(i);
        }

        Set<String> izomcsoportok = new HashSet<>(naploGyakOsszsuly.getIzomcsoportok());
        views.setTextViewText(R.id.tvNaploDatum, naploGyakOsszsuly.getNaplodatum());
        views.setTextViewText(R.id.tvIzomcsoportok, izomcsoportok.toString());
        views.setTextViewText(R.id.tvGyakOssz, osszsuly+" Kg");

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

    private List<NaploGyakOsszsuly> getNaploGyakOsszsuly(Context context) {
        System.out.println("Naplo getnaplo");
        Cursor naplocursor = context.getContentResolver().query(NaploCntAppWidget.NAPLO_URI, null, null,
                null, null);
        List<NaploGyakOsszsuly> naploGyakOsszsulies = new ArrayList<>();

//        if(naplocursor == null) return naploGyakOsszsulies;

        List<String> naploLista = getNaploList(naplocursor);
        naplocursor.close();
        List<String> gyakorlats;
        List<String> izomcsoportok;
        List<Integer> osszsuly;

        for (String naplo: naploLista) {
            gyakorlats = new ArrayList<>();
            osszsuly = new ArrayList<>();
            izomcsoportok = new ArrayList<>();
            Cursor c = context.getContentResolver().query(NaploCntAppWidget.GYAK_OSSZSULY_URI, null, naplo, null,null);
            while(c.moveToNext()) {
                izomcsoportok.add(c.getString(0));
                osszsuly.add(c.getInt(2));
                gyakorlats.add(c.getString(1));
            }
            naploGyakOsszsulies.add(new NaploGyakOsszsuly(naplo,izomcsoportok, gyakorlats, osszsuly));
            c.close();
        }
        return naploGyakOsszsulies;
    }

    private List<String> getNaploList(Cursor cursor) {
        List<String> naploList = new ArrayList<>();
        while (cursor.moveToNext()) {
            naploList.add(cursor.getString(1));
        }
        return naploList;
    }
}
