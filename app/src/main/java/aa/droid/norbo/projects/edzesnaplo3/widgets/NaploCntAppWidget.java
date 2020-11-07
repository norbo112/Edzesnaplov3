package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.MvvmBelepoActivity;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProviderWithHilt;

/**
 * Implementation of App Widget functionality.
 */
public class NaploCntAppWidget extends AppWidgetProvider {
    public static final Uri NAPLO_URI = Uri.parse("content://aa.dorid.norbo.projects.edzesnaplo3.contentprovider/naplo");
    public static final Uri GYAK_OSSZSULY_URI = Uri.parse("content://aa.dorid.norbo.projects.edzesnaplo3.contentprovider/gyakosszsuly");

    public static final String ACTION_RESZLETEZO = "reszletezo";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Cursor listnaplo = context.getContentResolver().query(NaploContentProviderWithHilt.GET_NAPLO, null, null,
                null, null);

        //teszt
        Intent intent = new Intent(context, ListItemService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.naplo_app_widget);
        views.setTextViewText(R.id.naplo_cnt_text, ((listnaplo != null) ? listnaplo.getCount()+" db napló":"0 db napló") + " rögzítve");
        views.setRemoteAdapter(R.id.listView, intent);

        Intent titleIntent = new Intent(context, MvvmBelepoActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.naplo_cnt_text, titlePendingIntent);

        Intent toastIntent = new Intent(context, NaploCntAppWidget.class);
        toastIntent.setAction(ACTION_RESZLETEZO);
        toastIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntentToast = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listView, pendingIntentToast);

        if(listnaplo != null) listnaplo.close();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, NaploCntAppWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn),
                    R.id.listView);
            for (int appwidgetid: appWidgetManager.getAppWidgetIds(cn)) {
                updateAppWidget(context, appWidgetManager, appwidgetid);
            }
        } else if(action.equals(ACTION_RESZLETEZO)) {
            NaploGyakOsszsuly naploGyakOsszsuly = (NaploGyakOsszsuly) intent.getSerializableExtra("naploadatok");
            String msg = "Elvégzett gyakorlatok: \n";
            for (int i = 0; i < naploGyakOsszsuly.getGyakorlats().size(); i++) {
                msg += naploGyakOsszsuly.getGyakorlats().get(i)+"\n";
            }
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
        super.onReceive(context, intent);
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

