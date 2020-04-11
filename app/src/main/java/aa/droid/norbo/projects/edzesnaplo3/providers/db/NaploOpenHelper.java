package aa.droid.norbo.projects.edzesnaplo3.providers.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NaploOpenHelper extends SQLiteOpenHelper {
    public static volatile NaploOpenHelper INSTANCE;

    public static NaploOpenHelper getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (NaploOpenHelper.class) {
                if(INSTANCE == null)
                    INSTANCE = new NaploOpenHelper(context.getApplicationContext());
            }
        }

        return INSTANCE;
    }

    public NaploOpenHelper(@Nullable Context context) {
        super(context, "edzesnaplo_db", null, 5);
    }

    public Cursor getGyakorlatListByNaplo(String naplodatum) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(
                "SELECT gyakorlattabla.csoport AS csoport, gyakorlattabla.megnevezes AS gynev, " +
                        "SUM(sorozattabla.suly * sorozattabla.ismetles) AS osszsuly FROM " +
                        "sorozattabla " +
                        "INNER JOIN gyakorlattabla ON sorozattabla.gyakorlatid = gyakorlattabla.id " +
                        "WHERE sorozattabla.naplodatum = '"+naplodatum+"' " +
                        "GROUP BY gynev", null);
    }

    public Cursor getNaploCursor() {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery("SELECT * FROM naplo",null);
    }

    public Cursor getSorozatSulyByNaplo(String naplodatum) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(
                "SELECT SUM(sorozattabla.suly * sorozattabla.ismetles) AS osszsuly, " +
                        "gyakorlattabla.megnevezes AS gynev"+
                        " FROM sorozattabla "+
                        " INNER JOIN gyakorlattabla ON sorozattabla.gyakorlatid = gyakorlattabla.id"+
                        " WHERE sorozattabla.naplodatum = '"+naplodatum+"'"+
                        " GROUP BY gynev ", null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
