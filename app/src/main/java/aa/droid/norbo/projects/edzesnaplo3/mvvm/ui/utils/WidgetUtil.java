package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploCntAppWidget;
import aa.droid.norbo.projects.edzesnaplo3.widgets.withhilt.EdzesnaploWidget;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class WidgetUtil {
    private Context context;

    @Inject
    public WidgetUtil(@ActivityContext Context context) {
        this.context = context;
    }

    /**
     * Frissíti az alkalmazás widgetjét...
     */
    public void updateWidget() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, EdzesnaploWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);
    }
}
