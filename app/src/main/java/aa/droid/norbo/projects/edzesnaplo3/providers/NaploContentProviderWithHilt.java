package aa.droid.norbo.projects.edzesnaplo3.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.components.ApplicationComponent;

public class NaploContentProviderWithHilt extends ContentProvider {
    public static final Uri GET_NAPLO = Uri.parse("content://aa.dorid.norbo.projects.edzesnaplo3.providers/naplo");

    @EntryPoint
    @InstallIn(ApplicationComponent.class)
    interface NaploContentProviderEntryPoint {
        NaploRepository naploRepository();
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Context context = getContext().getApplicationContext();
        NaploContentProviderEntryPoint entryPoint =
                EntryPointAccessors.fromApplication(context, NaploContentProviderEntryPoint.class);
        NaploRepository naploRepository = entryPoint.naploRepository();
        if(uri.equals(GET_NAPLO)) {
            return naploRepository.getNaploList();
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
