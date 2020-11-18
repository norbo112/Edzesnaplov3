package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProviderWithHilt;
import aa.droid.norbo.projects.edzesnaplo3.widgets.withhilt.EdzesnaploWidget;
import dagger.hilt.android.AndroidEntryPoint;

public class ListItemViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "ListItemViewFactory";
    private List<NaploGyakOsszsuly> naploGyakOsszsulyList;
    private Context context;
    private int appWidgetId;
    private SimpleDateFormat simpleDateFormat;
    private NaploRepository naploRepository;

    public ListItemViewFactory(Context context, Intent intent, NaploRepository naploRepository) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.naploRepository = naploRepository;
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Override
    public void onCreate() {
        naploGyakOsszsulyList = getNaploGyakOsszsuly();
    }

    @Override
    public void onDataSetChanged() {
        naploGyakOsszsulyList = getNaploGyakOsszsuly();
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
        fillInIntent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM,
                Long.toString(naploGyakOsszsuly.getNaplodatum()));
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

    private List<NaploGyakOsszsuly> getNaploGyakOsszsuly() {
        List<NaploGyakOsszsuly> naploGyakOsszsulies = new ArrayList<>();

//        Cursor c = context.getContentResolver().query(NaploContentProviderWithHilt.GET_NAPLO_GYAK_ES_OSSZSULY, null, null, null, null);
        Cursor c = naploRepository.getNaploOsszSulyBy();
        Log.i(TAG, "getNaploGyakOsszsuly: Naplo repository használva");
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
