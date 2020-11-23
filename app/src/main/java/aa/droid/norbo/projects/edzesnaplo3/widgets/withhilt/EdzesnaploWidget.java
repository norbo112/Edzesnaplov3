package aa.droid.norbo.projects.edzesnaplo3.widgets.withhilt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.GyakorlatListActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.MvvmBelepoActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import aa.droid.norbo.projects.edzesnaplo3.widgets.ListItemService;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
public class EdzesnaploWidget extends AppWidgetProvider {
    private static final String TAG = "EdzesnaploWidget";
    public static final String ADATOK_NAPLO = "NAPLO_ADATOK_EXTRA";
    public static final String ACTION_GYAKORLATOK = "aa.droid.norbo.projects.edzesnaplo3.EXTRA_GYAKORLATOK";
    public static final String ACTION_DETAILS = "aa.droid.norbo.projects.edzesnaplo3.ACTION_DETAILS";

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
        Intent toastIntent = new Intent(context, EdzesnaploWidget.class);
        PendingIntent pendingToast = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listView, pendingToast);

        if(naploList != null) naploList.close();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        Log.i(TAG, "onReceive: action="+action);

        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, EdzesnaploWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn),
                    R.id.listView);
            for (int appwidgetid: appWidgetManager.getAppWidgetIds(cn)) {
                updateAppWidget(context, appWidgetManager, appwidgetid);
            }
        } else if (action.equals(ACTION_DETAILS)) {
            Intent detailsIntent = new Intent(context, NaploDetailsActivity.class);
            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            detailsIntent.putExtra(NaploDetailsActivity.EXTRA_NAPLO, intent.getSerializableExtra(NaploDetailsActivity.EXTRA_NAPLO));
            context.startActivity(detailsIntent);
        } else if(action.equals(ACTION_GYAKORLATOK)) {
            String stringExtra = intent.getStringExtra(EdzesnaploWidget.ADATOK_NAPLO);
//            Intent gyaklistIntent = new Intent(context, GyakorlatListActivity.class);
//            gyaklistIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            gyaklistIntent.putExtra(ADATOK_NAPLO, stringExtra);
//            context.startActivity(gyaklistIntent);
            Toast.makeText(context, stringExtra, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

