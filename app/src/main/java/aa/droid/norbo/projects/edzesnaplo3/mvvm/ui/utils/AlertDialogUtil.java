package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.databinding.LoadingLayoutBinding;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class AlertDialogUtil {
    private Context context;

    @Inject
    public AlertDialogUtil(@ActivityContext Context context) {
        this.context = context;
    }

    public AlertDialog laodingDialog(Activity activity) {
        LoadingLayoutBinding binding = LoadingLayoutBinding.inflate(activity.getLayoutInflater());
        return new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false).create();
    }
}
