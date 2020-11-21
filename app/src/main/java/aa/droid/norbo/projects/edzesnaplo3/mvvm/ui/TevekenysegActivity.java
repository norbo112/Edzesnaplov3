package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityTestBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmAlertTevekenysegElhagyasaBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervManageUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.MvvmGyakorlatValasztoFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.TevekenysegFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.ViewPagerAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.AlertDialogUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegActivity extends BaseActivity<MvvmActivityTestBinding> implements AdatKozloInterface, EdzesTervManageUtil.TervValasztoInterface {
    private static final String TAG = "TestActivity";
    private GyakorlatUI gyakorlatUI;

    @Inject
    NaploWorker naploWorker;

    @Inject
    NaploListUtil naploListUtil;

    @Inject
    AlertDialogUtil alertDialogUtil;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    DateTimeFormatter formatter;

    @Inject
    EdzesTervManageUtil edzesTervManageUtil;

    @Inject
    SorozatUtil sorozatUtil;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    NaploViewModel naploViewModel;

    public TevekenysegActivity() {
        super(R.layout.mvvm_activity_test);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkSharedPrefForEdzesTerv();

        if (! getResources().getBoolean(R.bool.isTablet)) {
            ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
            myViewPagerAdapter.addFragment(new MvvmGyakorlatValasztoFragment(), "Gyakorlat");
            myViewPagerAdapter.addFragment(new TevekenysegFragment(), "Edzés");

            binding.viewPager.setAdapter(myViewPagerAdapter);
            binding.tabs.setupWithViewPager(binding.viewPager);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        naploWorker.getGyakorlatSzam().observe(this, integer -> {
            if(integer != 0)
                binding.toolbar.naploDetails.setText("Napló ("+integer+")");
            else
                binding.toolbar.naploDetails.setText("Napló");

            Log.i(TAG, "onCreate: Gyakorlatok száma: "+integer);
        });
    }

    private void checkSharedPrefForEdzesTerv() {
        String string = sharedPreferences.getString(VALASZTOTT_TERV_MEGNEVEZES, null);
        if(string != null) {
            edzesTervManageUtil.getEdzesTervById(this, sharedPreferences.getInt(VALASZTOTT_TERV_ID, 0))
                    .observe(this, this::valasztottTervFragmentErtesites);
        }
    }

    @Override
    public void onBackPressed() {
        MvvmAlertTevekenysegElhagyasaBinding viewBinding = MvvmAlertTevekenysegElhagyasaBinding.inflate(LayoutInflater.from(this), null, false);
        viewBinding.alertMessage.setText(R.string.mvvm_tevekenyseg_alert_backpressed);
        //alert
        new AlertDialog.Builder(this)
                .setTitle("Figyelem")
                .setView(viewBinding.getRoot())
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Visszalép", (dialog, which) -> super.onBackPressed())
                .show();
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar.customToolbar);
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setOnClickListener(v -> {
//                naploWorker.prepareUjGyakorlat(); itt ugye felveszi a aktuális sorozatokat is, de ezt inkább kézzel, mert lehet megszeretnénk nézni az eddigi müvelteket
                Naplo naplo = naploWorker.getNaplo();
                String title = "Napló "+formatter.getNaploDatum(naplo.getNaplodatum());
                title += String.format(Locale.getDefault(), " (%,.0f Kg)", naplo.getSorozats().stream().mapToDouble(s -> s.getSuly() * s.getIsmetles()).sum());
                title += " (mentés)";
                new AlertDialog.Builder(this)
//                        .setTitle("Napló "+formatter.getNaploDatum(naploWorker.getNaplo().getNaplodatum())+" (mentés)")
                        .setTitle(title)
                        .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, naplo.getSorozats()), null)
                        .setNeutralButton("ok", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("mentés", (dialog, which) -> {
                            if(naplo.getSorozats().size() != 0) {
                                naploWorker.saveNaplo();
                                finish();
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

        this.gyakorlatUI = gyakorlatUI;

        if (!getResources().getBoolean(R.bool.isTablet)) {
            binding.viewPager.setCurrentItem(1, true);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tevekenyseg_naplo_view) {
            AlertDialog dialog = alertDialogUtil.laodingDialog(this);
            dialog.show();
            naploViewModel.getNaploWithSorozat().observe(this, naploWithSorozats -> {
                if(naploWithSorozats != null && naploWithSorozats.size() > 0) {
                    naploListUtil.getNaploListDialog(naploWithSorozats).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Nincsenek mentett naplók", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (item.getItemId() == R.id.tevekenyseg_gyakorlat_view) {
            sorozatUtil.sorozatNezokeDialog(this, gyakorlatUI);
        } else if(item.getItemId() == R.id.tevekenyseg_edzesterv_valaszto) {
            edzesTervManageUtil.makeValasztoDialog(this, this, this);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void tervValasztva(EdzesTerv edzesTerv) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(VALASZTOTT_TERV_ID, edzesTerv.getTervId());
        edit.putString(VALASZTOTT_TERV_MEGNEVEZES, edzesTerv.getMegnevezes());
        if (edit.commit()) {
            valasztottTervFragmentErtesites(edzesTerv);
            Toast.makeText(this, "Terv kiválasztva: "+edzesTerv.getMegnevezes(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nem sikerült a kiválasztás", Toast.LENGTH_SHORT).show();
        }

    }

    public void valasztottTervFragmentErtesites(EdzesTerv edzesTerv) {
        for (Fragment fragment: getSupportFragmentManager().getFragments()) {
            if(fragment instanceof EdzesTervManageUtil.TervValasztoInterface) {
                ((EdzesTervManageUtil.TervValasztoInterface)fragment).tervValasztva(edzesTerv);
            }
        }
    }
}
