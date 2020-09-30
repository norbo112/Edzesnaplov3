package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmNaploDetailsActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.NaploDetailsRcViewAdapterFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NaploDetailsActivity extends BaseActiviry<MvvmNaploDetailsActivityBinding> implements NaploListFactory.NaploTorlesInterface {
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

//    @Inject
//    NaploListFactory naploListFactory;

    public NaploDetailsActivity() {
        super(R.layout.mvvm_naplo_details_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar.customToolbar);
        setupCustomActionBar();

        long naploDatum = getIntent().getLongExtra(EXTRA_NAPLO_DATUM, 0);

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

                binding.naploDetailsSulyLabel.setText(String.format(Locale.getDefault(), "Összesen %,d Kg megmozgatott súly",
                        sorozatWithGyakorlats.stream().mapToInt(gyak -> gyak.sorozat.getIsmetles() * gyak.sorozat.getSuly()).sum()));
                binding.naploDetailsInfoLabel.setText(String.format(Locale.getDefault(), "Elvégzett gyakorlatok száma [%d] db", getGyakDarabSzam(sorozatWithGyakorlats)));
            }
        });
    }

    private int getGyakDarabSzam(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        return sorozatWithGyakorlats.stream().map(sorozatWithGyakorlat -> sorozatWithGyakorlat.gyakorlat).collect(Collectors.toSet()).size();
    }

    @Override
    protected PopupMenu showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = super.showMoreOptionsPopupMenu(view);
        popupMenu.getMenu().removeItem(R.id.tevekenyseg_gyakorlat_view);
        popupMenu.getMenu().removeItem(R.id.tevekenyseg_naplo_view);
        return popupMenu;
    }

    @Override
    public void setupCustomActionBar() {
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setVisibility(View.GONE);
            binding.toolbar.moreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        }
    }

    @Override
    public void naplotTorol(long naplodatum) {
        naploViewModel.deleteNaplo(naplodatum);
        sorozatViewModel.deleteSorozat(naplodatum);
        Toast.makeText(this, "Napló törölve", Toast.LENGTH_SHORT).show();
    }
}
