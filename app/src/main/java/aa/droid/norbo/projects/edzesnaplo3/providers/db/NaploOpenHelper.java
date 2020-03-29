package aa.droid.norbo.projects.edzesnaplo3.providers.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NaploOpenHelper extends SQLiteOpenHelper {

    public NaploOpenHelper(@Nullable Context context) {
        super(context, "edzesnaplo_db", null, 4);
    }

    public Cursor getNaploCursor() {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("SELECT * FROM naplo",null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
