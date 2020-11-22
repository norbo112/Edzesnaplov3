package aa.droid.norbo.projects.edzesnaplo3.widgets.withhilt;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.MvvmBelepoActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import aa.droid.norbo.projects.edzesnaplo3.widgets.ListItemService;
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploGyakOsszsuly;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
public class EdzesnaploWidget extends AppWidgetProvider {
    public static final String ADATOK_NAPLO = "NAPLO_ADATOK_EXTRA";

    @Inject
    NaploRepository naploRepository;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        Cursor listnaplo = naploRepository.getNaploList();
        Cursor naploList = naploRepository.getNaploList();

        //teszt
        Intent intent = new Intent(context, ListItemService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.naplo_new_app_widget);
        views.setTextViewText(R.id.naplo_cnt_text, ((naploList != null) ? naploList.getCount()+" db napló":"0 db napló") + " rögzítve");
        views.setRemoteAdapter(R.id.listView, intent);


        Intent titleIntent = new Intent(context, MvvmBelepoActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.naplo_cnt_text, titlePendingIntent);

        //item on click
        Intent toastIntent = new Intent(context, NaploDetailsActivity.class);
        PendingIntent pendingIntentToast = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(toastIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listView, pendingIntentToast);

        if(naploList != null) naploList.close();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();

        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, EdzesnaploWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn),
                    R.id.listView);
            for (int appwidgetid: appWidgetManager.getAppWidgetIds(cn)) {
                updateAppWidget(context, appWidgetManager, appwidgetid);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor naploList = naploRepository.getNaploList();
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        if (naploList != null) {
            naploList.close();
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

