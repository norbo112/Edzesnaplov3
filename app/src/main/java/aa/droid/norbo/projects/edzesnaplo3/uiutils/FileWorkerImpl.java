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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;

public class FileWorkerImpl<T> implements FileWorkerInterface<T> {
    private static final String TAG = "FileWorker";
    private Context context;

    public FileWorkerImpl(Context context) {
        this.context = context;
    }

    @Override
    public Uri makeJsonFile(List<T> list, String filename) {
        Type type = new TypeToken<List<T>>(){}.getType();
        String json = new Gson().toJson(list, type);
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
                e.printStackTrace();
            }
        }
        return uri;
    }

    @Override
    public List<T> loadJsonFile(String jsonFileName) {
        BufferedReader reader = null;
        try {
            File file = new File(jsonFileName);
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Type typeToken = new TypeToken<List<T>>(){}.getType();
            return new Gson().fromJson(sb.toString(), typeToken);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "loadJsonFile: Nem található a "+jsonFileName+" fájl", e);
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
