package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatdialogBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters.GyakorlatItemAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.VideoUtils;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.VideoUtilsInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.report.SorozatReportUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatokActivity extends BaseActiviry<MvvmGyakorlatActivityBinding>
        implements VideoUtilsInterface, AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MvvmGyakorlatokActivity";
    private static final String VALASSZ_IZOMCSOP = "Mind látszik...";

    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    @Inject
    ModelConverter modelConverter;

    @Inject
    VideoUtils videoUtils;

    @Inject
    SorozatReportUtil tabletUtil;

    public MvvmGyakorlatokActivity() {
        super(R.layout.mvvm_gyakorlat_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getStringExtra(Intent.EXTRA_TEXT) != null) {
            sharedVideoLink(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }

        gyakorlatViewModel.getGyakorlatList().observe(this, gyakorlats -> {
            if (gyakorlats != null && gyakorlats.size() > 0) {
                binding.gyakorlatokLista.setAdapter(
                        new GyakorlatItemAdapter(gyakorlats.stream().map(gy -> modelConverter.fromEntity(gy)).collect(Collectors.toList()), MvvmGyakorlatokActivity.this));
                binding.gyakorlatokLista.setOnItemClickListener(this);

                if(isTablet())
                    initIzomcsoportSpinner(gyakorlats, binding.spinIzomcsop);
            } else {
                binding.gyakorlatokLista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        Stream.of("Nincsenek gyakorlatok rögzítve...", "Kérlek vegyél fel párat a hozzáadás gombbal").collect(Collectors.toList())));
            }
        });

        if (isTablet()) {
            binding.gyakReportActivityLayoutSrc.toolbar.setVisibility(View.GONE);
            if(kijelotGyakPoz != -1)
                initGyakDiagramok((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz));
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void initGyakDiagramok(GyakorlatUI gyakorlatUI) {
        tabletUtil.initSorozatReportCharts(this, gyakorlatUI.getId(), binding.gyakReportActivityLayoutSrc.osszsulyEsIsmChart,
                binding.gyakReportActivityLayoutSrc.elteltIdoChart);
    }

    private void sharedVideoLink(String aLink) {
        Log.i(TAG, "sharedVideoLink: brnnr vagyok");
        gyakorlatViewModel.getGyakorlatForVideoLinkEdit().whenComplete((gyakorlats, throwable) -> {
            Log.i(TAG, "sharedVideoLink: whenComplete");
            if (throwable != null) {
                runOnUiThread(() -> Toast.makeText(MvvmGyakorlatokActivity.this, "Hiba lépett fel", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "sharedVideoLink: gyakorlat betöltés hiba", throwable);
                return;
            }

            runOnUiThread(() -> videoUtils.createVideoSaveDialog(aLink, gyakorlats.stream().map(gy -> modelConverter.fromEntity(gy)).collect(Collectors.toList()), this));
        });
    }

    int kijelotGyakPoz = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        kijelotGyakPoz = position;
        if(isTablet())
            initGyakDiagramok((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz));
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.drawable.ic_gyakorlatok_kis_logo);
            getSupportActionBar().setTitle(R.string.welcome_gyakorlatok_title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mvvm_gyakorlatok_search_menu, menu);
        //kereső aktiválása
        MenuItem searchViewItem = menu.findItem(R.id.gyakorlatokSearch);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((GyakorlatItemAdapter) binding.gyakorlatokLista.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void showAlertGyakTorles(Gyakorlat gyakorlat) {
        new AlertDialog.Builder(this)
                .setMessage("Biztos törölni akarod?")
                .setTitle(gyakorlat.getMegnevezes() + " törlése")
                .setPositiveButton("Igen", (dialog, which) -> {
                    gyakorlatViewModel.delete(gyakorlat);
                    Toast.makeText(this, gyakorlat.getMegnevezes() + " törlésre került", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Nem", (dialog, which) -> {
                })
                .create()
                .show();
    }

    private void createGyakorlatDialog(Gyakorlat gyakorlat) {
        MvvmGyakorlatdialogBinding gyakBinding = DataBindingUtil.inflate(
                getLayoutInflater(), R.layout.mvvm_gyakorlatdialog, null, false);
        String title = (gyakorlat != null) ? gyakorlat.getMegnevezes() + " szerkesztése" : "Új gyakorlat felvétele";
        String[] izomccsoportResource = getResources().getStringArray(R.array.izomcsoportok);
        if (gyakorlat != null) {
            int szerkeszIndex = 0;
            for (int i = 0; i < izomccsoportResource.length; i++) {
                if (izomccsoportResource[i].equals(gyakorlat.getCsoport())) {
                    szerkeszIndex = i;
                    break;
                }
            }
            gyakBinding.etGyakDialogCsoport.setSelection(szerkeszIndex);
            gyakBinding.setGyakorlat(modelConverter.fromEntity(gyakorlat));
        } else {
            gyakBinding.setGyakorlat(new GyakorlatUI());
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(gyakBinding.getRoot())
                .setPositiveButton("OK", (dialog, which) -> {
                    String valasztottCsoport = gyakBinding.etGyakDialogCsoport.getSelectedItem().toString();

                    if (TextUtils.isEmpty(gyakBinding.etGyakDialogNev.getText().toString()) ||
                            valasztottCsoport.equals("Kérlek, válassz...")) {
                        Toast.makeText(this, "Izomcsoport, megnevezés kötelező megadni", Toast.LENGTH_LONG).show();
                        return;
                    }

                    GyakorlatUI gyakorlat1 = gyakBinding.getGyakorlat();

                    setGyakorlatAdat(gyakBinding, gyakorlat1);

                    if (gyakorlat == null) {
                        gyakorlat1.setCsoport(valasztottCsoport);
                        gyakorlatViewModel.insert(modelConverter.fromUI(gyakorlat1));
                        Toast.makeText(this, "Gyakorlat felvéve a listára", Toast.LENGTH_SHORT).show();
                    } else {
                        gyakorlatViewModel.update(modelConverter.fromUI(gyakorlat1));
                        Toast.makeText(this, "Gyakorlat szerkesztve", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Mégse", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void setGyakorlatAdat(MvvmGyakorlatdialogBinding gyakBinding, GyakorlatUI gyakorlat1) {
        if (TextUtils.isEmpty(gyakBinding.etGyakDialogLeiras.getText().toString()))
            gyakorlat1.setLeiras("");

        if (TextUtils.isEmpty(gyakBinding.etGyakDialogVideolink.getText().toString()))
            gyakorlat1.setVideolink("");

        if (TextUtils.isEmpty(gyakBinding.etGyakDialogVideoStartPoz.getText().toString()))
            gyakorlat1.setVideostartpoz("0");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ujgyak :
                createGyakorlatDialog(null);
                break;
            case R.id.gyakszerk:
                if(kijelotGyakPoz == -1) {
                    Toast.makeText(this, "Kérlek válassz egy gyakorlatot!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                createGyakorlatDialog(modelConverter.fromUI(
                        (GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz)
                ));
                break;
            case R.id.gyaktorol:
                if(kijelotGyakPoz == -1) {
                    Toast.makeText(this, "Kérlek válassz egy gyakorlatot!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                showAlertGyakTorles(modelConverter.fromUI(
                        (GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz)
                ));
                break;
            case R.id.gyakszerk_menu_video:
                if(kijelotGyakPoz == -1) {
                    Toast.makeText(this, "Kérlek válassz egy gyakorlatot!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Gyakorlat gyakorlat = modelConverter.fromUI(
                        (GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz)
                );
                if (gyakorlat != null && gyakorlat.getVideolink().length() > 0) {
                    Intent videointent = new Intent(this, VideoActivity.class);
                    videointent.putExtra(VideoActivity.EXTRA_GYAKORLAT, gyakorlat);
                    startActivity(videointent);
                } else {
                    Toast.makeText(this, "Nincs video!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gyakdiagram :
                if(kijelotGyakPoz == -1) {
                    Toast.makeText(this, "Kérlek válassz egy gyakorlatot!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = new Intent(this, MvvmGyakorlatReportActivity.class);
                intent.putExtra(MvvmGyakorlatReportActivity.EXTRA_GYAK,
                        modelConverter.fromUI((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijelotGyakPoz)));
                startActivity(intent);
                overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                break;
        }
        return true;
    }

    private void initIzomcsoportSpinner(List<Gyakorlat> gyakorlats, Spinner spinner) {
        List<String> izomcsoportList = new ArrayList<>();
        izomcsoportList.add(VALASSZ_IZOMCSOP);
        izomcsoportList.addAll(gyakorlats.stream().map(Gyakorlat::getCsoport).distinct().collect(Collectors.toList()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, izomcsoportList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Filter filter = ((GyakorlatItemAdapter) binding.gyakorlatokLista.getAdapter()).getFilter();
                    filter.filter(spinner.getSelectedItem().equals(VALASSZ_IZOMCSOP) ? "" : spinner.getSelectedItem().toString());
                } catch (NullPointerException ex) {
                    Log.i(TAG, "onItemSelected: null pointer filter!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}