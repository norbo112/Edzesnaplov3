package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityMentettNaplokBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmSavedNaploActivity extends BaseActiviry<MvvmActivityMentettNaplokBinding> implements NaploListFactory.NaploTorlesInterface {
    @Inject
    NaploViewModel naploViewModel;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    NaploListFactory naploListFactory;

    @Inject
    DateTimeFormatter dateTimeFormatter;

    public MvvmSavedNaploActivity() {
        super(R.layout.mvvm_activity_mentett_naplok);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.mentettNaplokWarningLabel.setVisibility(View.VISIBLE);

        naploViewModel.getNaploWithSorozat().observe(this, naplos -> {
            if(naplos != null && naplos.size() > 0) {
                binding.mentettNaplokWarningLabel.setVisibility(View.GONE);
                binding.mentettNaplokLista.setAdapter(naploListFactory.getListAdapter(naplos));
                binding.mentettNaplokLista.setOnItemClickListener((parent, view, position, id) -> {
                    NaploWithSorozat item = (NaploWithSorozat) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(this, NaploDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, item.daonaplo.getNaplodatum());
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                });
            }
        });
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar.customToolbar);
        if(getSupportActionBar() != null)
            binding.toolbar.naploDetails.setVisibility(View.GONE);
    }

    @Override
    public void naplotTorol(long naplodatum) {
        new AlertDialog.Builder(this)
                .setTitle("Törlés?")
                .setMessage(dateTimeFormatter.getNaploDatum(naplodatum)+" valóban törölni akarod?")
                .setPositiveButton("ok", (dialog, which) -> {
                    naploViewModel.deleteNaplo(naplodatum);
                    sorozatViewModel.deleteSorozat(naplodatum);
                    Toast.makeText(this, "Sikeresen törölve a napló!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
