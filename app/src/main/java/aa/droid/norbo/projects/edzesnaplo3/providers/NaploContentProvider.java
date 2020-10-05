package aa.droid.norbo.projects.edzesnaplo3.providers;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploCntAppWidget;

public class NaploContentProvider extends ContentProvider {
    public static final Uri GET_NAPLO = Uri.parse("content://aa.dorid.norbo.projects.edzesnaplo3.contentprovider/naplo");
    public static final Uri GET_GYAK_OSSZSULY = Uri.parse("content://aa.dorid.norbo.projects.edzesnaplo3.contentprovider/gyakosszsuly");
    public NaploContentProvider() {
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, NaploCntAppWidget.class));
        context.sendBroadcast(intent);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
//        if(uri.equals(GET_NAPLO)) {
//            NaploDao naploDao = EdzesNaploDatabase.getDatabase(getContext()).naploDao();
//            return naploDao.selectAll();
//        } else if(uri.equals(GET_GYAK_OSSZSULY)) {
//            SorozatDao sorozatDao = EdzesNaploDatabase.getDatabase(getContext()).sorozatDao();
//            return sorozatDao.selectAll(selection);
//        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
