package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmNaploDetailsActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.NaploDetailsRcViewAdapterFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;

public class NaploDetailsActivity extends BaseActiviry<MvvmNaploDetailsActivityBinding> {
    public static final String EXTRA_NAPLO_DATUM = "aa.droid.norbo.projects.edzesnaplo3.v4.EXTRA_NAPLO_DATUM";
    public static final String EXTRA_NAPLO_LABEL = "aa.droid.norbo.projects.edzesnaplo3.v4.EXTRA_NAPLO_LABEL";

    @Inject
    NaploDetailsRcViewAdapterFactory adapterFactory;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    DateTimeFormatter dateTimeFormatter;

    @Inject
    NaploViewModel naploViewModel;

    public NaploDetailsActivity() {
        super(R.layout.mvvm_naplo_details_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar.customToolbar);

        long naploDatum = getIntent().getLongExtra(EXTRA_NAPLO_DATUM, 0);
        long naploLabel = getIntent().getLongExtra(EXTRA_NAPLO_LABEL, 0);

        if(naploDatum != 0) {
            setupRcViewWithDate(naploDatum);
        }
    }

    private void setupRcViewWithDate(long naploDatum) {
        sorozatViewModel.getForNaplo(naploDatum).observe(this, sorozatWithGyakorlats -> {
            if(sorozatWithGyakorlats != null) {
                binding.naploDetailsDatumLabel.setText(dateTimeFormatter.getNaploDatum(naploDatum));
                binding.naploDetailsRcView.setAdapter(adapterFactory.create(sorozatWithGyakorlats));
                binding.naploDetailsRcView.setItemAnimator(new DefaultItemAnimator());
                binding.naploDetailsRcView.setLayoutManager(new LinearLayoutManager(NaploDetailsActivity.this, RecyclerView.HORIZONTAL, false));
            }
        });
    }


    @Override
    public void setupCustomActionBar() {
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setOnClickListener(v -> {
                naploViewModel.getNaploList().observe(this, naplos -> {
                    ArrayAdapter<Naplo> listAdapter = new ArrayAdapter<>(NaploDetailsActivity.this, android.R.layout.simple_list_item_1, naplos);
                    new AlertDialog.Builder(this)
                            .setTitle("Mentett naplók")
                            .setAdapter(listAdapter, (dialog, which) -> {
                                Naplo naplo = listAdapter.getItem(which);
                                if(naplo != null) {
                                    setupRcViewWithDate(naplo.getNaplodatum());
                                } else {
                                    Toast.makeText(this, "Nem lehet megtekinteni a naplót :(", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                            .show();
                });
            });
        }
    }
}
