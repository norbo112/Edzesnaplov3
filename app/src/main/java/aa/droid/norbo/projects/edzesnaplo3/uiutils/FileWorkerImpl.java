package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
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

public class FileWorkerImpl implements FileWorkerInterface {
    private static final String TAG = "FileWorker";
    private Context context;

    public FileWorkerImpl(Context context) {
        this.context = context;
    }

    @Override
    public Uri makeJsonFile(List<SorozatWithGyakorlat> list, String filename) {
        String json = new Gson().toJson(list);
        BufferedWriter writer = null;
        Uri uri = null;
        File file = new File(
                context.getExternalFilesDir(null),
                filename);
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
    public List<SorozatWithGyakorlat> loadJsonFile(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Type typeToken = new TypeToken<List<SorozatWithGyakorlat>>(){}.getType();
            return new Gson().fromJson(sb.toString(), typeToken);
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
