package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProviderWithHilt;

public class ListItemViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<NaploGyakOsszsuly> naploGyakOsszsulyList;
    private Context context;
    private int appWidgetId;
    private SimpleDateFormat simpleDateFormat;

    public ListItemViewFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        NaploGyakOsszsuly naploGyakOsszsuly = naploGyakOsszsulyList.get(position);

        views.setTextViewText(R.id.tvNaploDatum, simpleDateFormat.format(new Date(naploGyakOsszsuly.getNaplodatum())));
        views.setTextViewText(R.id.tvGyakOssz,
                String.format(Locale.getDefault(), "%,d Kg", naploGyakOsszsuly.getGyakorlatOsszsulys()));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("naploadatok", naploGyakOsszsuly);
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

    private List<NaploGyakOsszsuly> getNaploGyakOsszsuly(Context context) {
        List<NaploGyakOsszsuly> naploGyakOsszsulies = new ArrayList<>();

        Cursor c = context.getContentResolver().query(NaploContentProviderWithHilt.GET_NAPLO_GYAK_ES_OSSZSULY, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                naploGyakOsszsulies.add(new NaploGyakOsszsuly(
                        Long.parseLong(c.getString(1)),
                        Integer.parseInt(c.getString(0))));
            }
            c.close();
        }
        return naploGyakOsszsulies;
    }
}
