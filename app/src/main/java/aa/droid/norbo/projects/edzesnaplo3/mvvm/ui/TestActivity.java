package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
public class TestActivity extends BaseActiviry<MvvmActivityTestBinding> implements AdatKozloInterface {
    private static final String TAG = "TestActivity";

    @Inject
    AdatFeltoltes adatFeltoltes;

    @Inject
    NaploWorker naploWorker;

    public TestActivity() {
        super(R.layout.mvvm_activity_test);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adatFeltoltes.gyakorlatAdatFeltoltes();

        binding.toolbar.setTitle("Edzésnapló v3");
        binding.toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(binding.toolbar);

        ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        myViewPagerAdapter.addFragment(new MvvmGyakorlatValasztoFragment(), "Gyakorlat");
        myViewPagerAdapter.addFragment(new TevekenysegFragment(), "Edzés");

        binding.viewPager.setAdapter(myViewPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);

        naploWorker.getGyakorlatSzam().observe(this, integer -> {
            if(integer != 0)
                myViewPagerAdapter.setPageTitle(1, "Edzés ("+integer+")");
            else
                myViewPagerAdapter.setPageTitle(1, "Edzés ");

            Log.i(TAG, "onCreate: Gyakorlatok száma: "+integer);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mvvm_tevekenyseg, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mvvm_tevekenyseg_naplo_menu) {
            new AlertDialog.Builder(this)
                    .setTitle("Napló részletek")
                    .setMessage(naploWorker.getNaplo().toString())
                    .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                    .show();
        }
        return super.onOptionsItemSelected(item);
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
