package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class DialogFactory {
    private Context context;

    @Inject
    public DialogFactory(@ActivityContext Context context) {
        this.context = context;
    }

    public void showMentettNaplok(List<Naplo> naplos) {
        if(naplos.size()>0) {
            ArrayAdapter<Naplo> listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, naplos);
            new AlertDialog.Builder(context)
                    .setTitle("Mentett naplók")
                    .setAdapter(listAdapter, (dialog, which) -> {
                        Naplo naplo = listAdapter.getItem(which);
                        if (naplo != null) {
                            Intent intent = new Intent(context, NaploDetailsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, naplo.getNaplodatum());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Nem lehet megtekinteni a naplót :(", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setMessage("Nincsenek még mentve adatok")
                    .show();
        }
    }
}
