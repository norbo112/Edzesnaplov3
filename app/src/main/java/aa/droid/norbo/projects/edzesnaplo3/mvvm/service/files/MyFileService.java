package aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithOnlySorozats;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.NaploAll;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class MyFileService {
    private static final String TAG = "FilaService";
    private Context context;
    private ExecutorService fileExecutorService;
    private NaploViewModel naploViewModel;

    @Inject
    public MyFileService(@ActivityContext Context context, ExecutorService executorService, NaploViewModel naploViewModel) {
        this.context = context;
        this.fileExecutorService = executorService;
        this.naploViewModel = naploViewModel;
    }

    private Uri fileSave(NaploWithOnlySorozats naplo) {
        Uri fileUri = null;
        File file = new File(context.getExternalFilesDir(null), "naplo_"+naplo.naplo.getNaplodatum()+"_saved.naplo");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(new Gson().toJson(naplo));

            if(file.exists()) {
                fileUri = Uri.parse(file.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e(TAG, "fileSave: Fájl írási/olvasási hiba", e);
        }

        return fileUri;
    }

    private NaploWithOnlySorozats loadSaveFile(Uri data) {
        try(InputStreamReader inputStream = new InputStreamReader(context.getContentResolver().openInputStream(data))) {
            return new Gson().fromJson(inputStream, NaploWithOnlySorozats.class);
        } catch (IOException | NullPointerException | JsonSyntaxException e) {
            Log.e(TAG, "loadSaveFile: ", e);
            return null;
        }
    }

    public CompletableFuture<Uri> futureFileSave(long naplodatum) {
        return CompletableFuture.supplyAsync(() -> {
            NaploWithOnlySorozats naploWithOnlySorozats = naploViewModel.getSyncNaploWithOnlySorozats(naplodatum);
            return fileSave(naploWithOnlySorozats);
        }, fileExecutorService);
    }

    public CompletableFuture<NaploWithOnlySorozats> futureLoadSaveFile(Uri data) {
        return CompletableFuture.supplyAsync(() -> loadSaveFile(data));
    }
}
