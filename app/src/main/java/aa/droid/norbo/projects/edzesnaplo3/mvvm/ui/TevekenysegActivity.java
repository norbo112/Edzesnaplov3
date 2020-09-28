package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityTestBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.utils.AdatFeltoltes;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.MvvmGyakorlatValasztoFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.TevekenysegFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.ViewPagerAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegActivity extends BaseActiviry<MvvmActivityTestBinding> implements AdatKozloInterface {
    private static final String TAG = "TestActivity";

    @Inject
    AdatFeltoltes adatFeltoltes;

    @Inject
    NaploWorker naploWorker;

    public TevekenysegActivity() {
        super(R.layout.mvvm_activity_test);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adatFeltoltes.gyakorlatAdatFeltoltes();

//        binding.toolbar.setTitle("Edzésnapló v3");
//        binding.toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(binding.toolbar.customToolbar);
        setupCustomActionBar();

        ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        myViewPagerAdapter.addFragment(new MvvmGyakorlatValasztoFragment(), "Gyakorlat");
        myViewPagerAdapter.addFragment(new TevekenysegFragment(), "Edzés");

        binding.viewPager.setAdapter(myViewPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);

        naploWorker.getGyakorlatSzam().observe(this, integer -> {
            if(integer != 0)
                binding.toolbar.naploDetails.setText("Napló ("+integer+")");
            else
                binding.toolbar.naploDetails.setText("Napló");

            Log.i(TAG, "onCreate: Gyakorlatok száma: "+integer);
        });
    }

    @Override
    public void setupCustomActionBar() {
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setOnClickListener(v -> {
                naploWorker.prepareUjGyakorlat();
                new AlertDialog.Builder(this)
                        .setTitle("Napló részletek (mentés)")
                        .setMessage(naploWorker.getNaplo().toString())
                        .setNeutralButton("ok", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("mentés", (dialog, which) -> {
                            if(naploWorker.getNaplo().getSorozats().size() != 0) {
                                naploWorker.saveNaplo();
                                Toast.makeText(this, "Napló mentve!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Nincs mit menteni!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            });

            binding.toolbar.moreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        }
    }

    @Override
    public void gyakorlatAtado(GyakorlatUI gyakorlatUI) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment: fragments) {
            if(fragment instanceof TevekenysegFragment) {
                ((TevekenysegFragment)fragment).gyakorlatAtado(gyakorlatUI);
            }
        }

        binding.viewPager.setCurrentItem(1, true);
    }
}
