package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityTestBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmAlertTevekenysegElhagyasaBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmKorabbiSorozatItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.utils.AdatFeltoltes;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervValasztoDialog;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.MvvmGyakorlatValasztoFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.TevekenysegFragment;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.ViewPagerAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.KorabbiSorozatRcViewAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegActivity extends BaseActiviry<MvvmActivityTestBinding> implements AdatKozloInterface, EdzesTervValasztoDialog.TervValasztoInterface {
    private static final String TAG = "TestActivity";
    private Integer gyakorlatId;

    @Inject
    NaploWorker naploWorker;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    DateTimeFormatter formatter;

    @Inject
    EdzesTervValasztoDialog tervValasztoDialog;

    @Inject
    SharedPreferences sharedPreferences;

    public TevekenysegActivity() {
        super(R.layout.mvvm_activity_test);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                naploWorker.prepareUjGyakorlat();
                new AlertDialog.Builder(this)
                        .setTitle("Napló "+formatter.getNaploDatum(naploWorker.getNaplo().getNaplodatum())+" (mentés)")
//                        .setMessage()
                        .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, naploWorker.getNaplo().getSorozats()), null)
                        .setNeutralButton("ok", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("mentés", (dialog, which) -> {
                            if(naploWorker.getNaplo().getSorozats().size() != 0) {
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
                gyakorlatId = gyakorlatUI.getId();
            }
        }

        if (!getResources().getBoolean(R.bool.isTablet)) {
            binding.viewPager.setCurrentItem(1, true);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tevekenyseg_naplo_view) {
            Toast.makeText(this, "Naplók megtekintése TV", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.tevekenyseg_gyakorlat_view) {
            korabbiSorozatokMegtekintese();
        } else if(item.getItemId() == R.id.tevekenyseg_edzesterv_valaszto) {
            tervValasztoDialog.makeValasztoDialog(this, this, this);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void tervValasztva(EdzesTerv edzesTerv) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(VALASZTOTT_TERV, edzesTerv.getTervId());
        if (edit.commit()) {
            Toast.makeText(this, "Terv kiválasztva: "+edzesTerv.getMegnevezes(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nem sikerült a kiválasztás", Toast.LENGTH_SHORT).show();
        }

    }

    private void korabbiSorozatokMegtekintese() {
        if(gyakorlatId == null) {
            Toast.makeText(this, "Kérlek válassz egy gyakorlatot a megtekintéshez!", Toast.LENGTH_SHORT).show();
            return;
        }

        sorozatViewModel.getSorozatByGyakorlat(gyakorlatId).observe(this, sorozats -> {
            if(sorozats != null && sorozats.size() > 0) {
                ArrayAdapter<NaploEsSorozat> listAdapter = new ArrayAdapter<NaploEsSorozat>(this, R.layout.mvvm_korabbi_sorozat_item, makeNaploEsSorozat(sorozats)) {
                    MvvmKorabbiSorozatItemBinding itemBinding;
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if(convertView == null) {
                            convertView = LayoutInflater.from(TevekenysegActivity.this).inflate(R.layout.mvvm_korabbi_sorozat_item, parent, false);
                            itemBinding = DataBindingUtil.bind(convertView);
                            convertView.setTag(itemBinding);
                        } else {
                            itemBinding = (MvvmKorabbiSorozatItemBinding) convertView.getTag();
                        }

                        NaploEsSorozat item = getItem(position);
                        itemBinding.korabbiSorozatDatumLabel.setText(formatter.getNaploDatum(item.naplodatum)+" "+getSorozatOsszSuly(item.sorozats));
                        itemBinding.korabbiSorozatLista.setAdapter(new KorabbiSorozatRcViewAdapter(item.sorozats, formatter));
                        itemBinding.korabbiSorozatLista.setLayoutManager(new LinearLayoutManager(TevekenysegActivity.this, RecyclerView.HORIZONTAL, false));
                        return itemBinding.getRoot();
                    }
                };

                new AlertDialog.Builder(this)
                        .setTitle("Sorozatok")
                        .setAdapter(listAdapter, null)
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Toast.makeText(this, "Nincs rögzítve még sorozat evvel a gyakorlattal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<NaploEsSorozat> makeNaploEsSorozat(List<Sorozat> sorozats) {
        List<NaploEsSorozat> list = new ArrayList<>();
        Set<Long> naploDatumok = sorozats.stream().map(Sorozat::getNaplodatum).collect(Collectors.toSet());
        naploDatumok.forEach(aLong -> {
            NaploEsSorozat naploEsSorozat = new NaploEsSorozat(aLong);
            List<Sorozat> sorozats1 = new ArrayList<>();
            sorozats.forEach(sorozat -> {
                if(aLong == (sorozat.getNaplodatum()))
                    sorozats1.add(sorozat);
            });
            naploEsSorozat.setSorozats(sorozats1);
            list.add(naploEsSorozat);
        });
        list.sort((o1, o2) -> Long.compare(o2.naplodatum, o1.naplodatum));
        return list;
    }

    private String getSorozatOsszSuly(List<Sorozat> sorozats) {
        return String.format(Locale.getDefault(), "%,d Kg", sorozats.stream().mapToInt(sor -> sor.getSuly() * sor.getIsmetles()).sum());
    }

    public class NaploEsSorozat {
        private long naplodatum;
        private List<Sorozat> sorozats;

        public NaploEsSorozat(long naplodatum) {
            this.naplodatum = naplodatum;
        }

        public void setSorozats(List<Sorozat> sorozats) {
            this.sorozats = sorozats;
        }

        public List<Sorozat> getSorozats() {
            return sorozats;
        }

        public long getNaplodatum() {
            return naplodatum;
        }
    }
}
