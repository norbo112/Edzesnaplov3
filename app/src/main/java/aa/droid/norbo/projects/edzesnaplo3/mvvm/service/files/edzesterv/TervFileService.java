package aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files.edzesterv;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.NaploAll;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class TervFileService {
    private static final String TAG = "TervFileService";
    private Context context;
    private ExecutorService fileExecutorService;

    @Inject
    public TervFileService(@ActivityContext Context context, ExecutorService executorService) {
        this.context = context;
        this.fileExecutorService = executorService;
    }

    private Uri fileSave(EdzesTerv edzesTerv) {
        Uri fileUri = null;
        File file = new File(context.getExternalFilesDir(null), "naplo_terv_"+System.currentTimeMillis()+"_saved.edzesTerv");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(new Gson().toJson(edzesTerv));

            if(file.exists()) {
                fileUri = Uri.parse(file.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e(TAG, "fileSave: Fájl írási/olvasási hiba", e);
        }

        return fileUri;
    }

    private EdzesTerv loadSaveFile(Uri data) {
        try(InputStreamReader inputStream = new InputStreamReader(context.getContentResolver().openInputStream(data))) {
            return new Gson().fromJson(inputStream, EdzesTerv.class);
        } catch (IOException | NullPointerException | JsonSyntaxException e) {
            Log.e(TAG, "loadSaveFile: ", e);
            return null;
        }
    }

    public CompletableFuture<Uri> futureFileSave(EdzesTerv edzesTervs) {
        return CompletableFuture.supplyAsync(() -> fileSave(edzesTervs), fileExecutorService);
    }

    public CompletableFuture<EdzesTerv> futureLoadSaveFile(Uri data) {
        return CompletableFuture.supplyAsync(() -> loadSaveFile(data));
    }
}
