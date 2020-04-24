package aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.interfaces.FileWorker2Interface;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models.GyakorlatCsomag;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.fileworkers.models.NaploAll;

public class FileWorker2Impl implements FileWorker2Interface {
    private static final String TAG = "FileWorker";
    private Context context;

    public FileWorker2Impl(Context context) {
        this.context = context;
    }

    @Override
    public Uri makeJsonFile(List<SorozatWithGyakorlat> sorozatWithGyakorlats,
                            Naplo naplo) {
        if(naplo.getCommentFilePath() == null) naplo.setCommentFilePath("");
        String json = new Gson().toJson(new NaploAll(sorozatWithGyakorlats, naplo));

        BufferedWriter writer = null;
        Uri uri = null;
        File file = new File(
                context.getExternalFilesDir(null),
                "naplo_"+System.currentTimeMillis()+".json");
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json);

            if (file.exists()) {
                uri = Uri.parse(file.getAbsolutePath());
            }
        } catch (Exception ex) {
            Log.e(TAG, "makeJsonFile: " + ex);
        } finally {
            try {
                if(writer!=null) writer.close();
            } catch (IOException e) {
            }
        }
        return uri;
    }

    @Override
    public NaploAll loadJsonFile(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

//            Type typeToken = new TypeToken<NaploAll>(){}.getType();
            return new Gson().fromJson(sb.toString(), NaploAll.class);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "loadJsonFile: Nem található a fájl", e);
        } catch (IOException e) {
            Log.e(TAG, "loadJsonFile: Olvasási hiba", e);
        } finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e){}
            }
        }
        return null;
    }
}
