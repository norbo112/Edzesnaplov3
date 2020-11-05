package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatVideoLinkSharedBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatdialogBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters.GyakorlatItemAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatokActivity extends BaseActiviry<MvvmGyakorlatActivityBinding> implements AdapterView.OnItemClickListener {
    private static final String TAG = "MvvmGyakorlatokActivity";

    private AlertDialog alertDialog;

    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    @Inject
    ModelConverter modelConverter;

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
                binding.gyakorlatokLista.setNestedScrollingEnabled(true);
            } else {
                binding.gyakorlatokLista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        Stream.of("Nincsenek gyakorlatok rögzítve...", "Kérlek vegyél fel párat a hozzáadás gombbal").collect(Collectors.toList())));
            }
        });

        binding.fab.setOnClickListener(v -> createGyakorlatDialog(null));


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

            runOnUiThread(() -> createVideoSaveDialog(aLink, gyakorlats.stream().map(gy -> modelConverter.fromEntity(gy)).collect(Collectors.toList())));
        });
    }

    private void createVideoSaveDialog(String aLink, List<GyakorlatUI> gyakorlatUIS) {
        if (gyakorlatUIS != null && gyakorlatUIS.size() > 0) {
            Log.i(TAG, "createVideoSaveDialog: gyakotlaz nem null");
            MvvmGyakorlatVideoLinkSharedBinding binding = MvvmGyakorlatVideoLinkSharedBinding.inflate(getLayoutInflater());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(binding.getRoot());
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String link = aLink.substring(aLink.lastIndexOf('/') + 1);
            binding.gyakorlatVideoLink.setText(link);

//            ArrayAdapter<GyakorlatUI> adapter = new ArrayAdapter<>(MvvmGyakorlatokActivity.this, android.R.layout.simple_list_item_1, gyakorlatUIS);
            GyakorlatItemAdapter adapter = new GyakorlatItemAdapter(gyakorlatUIS, this);
            binding.gyakSpinner.setAdapter(adapter);

            List<String> izomcsoportok = new ArrayList<>();
            izomcsoportok.add("Válassz...");
            izomcsoportok.addAll(gyakorlatUIS.stream().map(gyakorlatUI -> gyakorlatUI.getCsoport()).distinct().collect(Collectors.toList()));
            ArrayAdapter<String> izomcsoportokAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, izomcsoportok);
            izomcsoportokAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            binding.spinIzomcsop.setAdapter(izomcsoportokAdapter);
            binding.spinIzomcsop.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Filter filter = ((GyakorlatItemAdapter) binding.gyakSpinner.getAdapter()).getFilter();
                            if (filter != null) {
                                String constraint = parent.getAdapter().getItem(position).toString();
                                if(constraint.equals("Válassz...")) constraint = "";
                                filter.filter(constraint);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }
            );

            binding.gyakorlatVideoBtnMentesMegse.setOnClickListener(v -> finish());
            binding.gyakorlatVideoBtnMentes.setOnClickListener(v -> {
                GyakorlatUI gyakorlatUI = (GyakorlatUI) binding.gyakSpinner.getSelectedItem();
                if (gyakorlatUI.getVideolink() != null && gyakorlatUI.getVideolink().length() > 1) {
                    Toast.makeText(MvvmGyakorlatokActivity.this, "Ehhez a gyakorlathoz már van mentve videó link!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(binding.gyakorlatVideoLink.getText().toString()) && binding.gyakorlatVideoLink.getText().toString().length() > 5) {
                    gyakorlatUI.setVideolink(binding.gyakorlatVideoLink.getText().toString());
                } else {
                    Toast.makeText(MvvmGyakorlatokActivity.this, "Videó azonosító hiányzik", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(binding.gyakorlatVideoLinkPoz.getText().toString())) {
                    gyakorlatUI.setVideostartpoz(binding.gyakorlatVideoLinkPoz.getText().toString());
                } else {
                    gyakorlatUI.setVideostartpoz("0");
                }

                gyakorlatViewModel.update(modelConverter.fromUI(gyakorlatUI));
                Toast.makeText(MvvmGyakorlatokActivity.this, "Gyakorlat videó rögzítve!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });

            alertDialog.show();
        } else {
            Toast.makeText(MvvmGyakorlatokActivity.this, "Nincs gyakorlat rögzítve amihez hozzálehetne adni a videót", Toast.LENGTH_SHORT).show();
        }
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

    int kijeloltGyakPoz;

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        kijeloltGyakPoz = position;

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.gyak_activity_szerkeszto_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, (MenuBuilder) popupMenu.getMenu(), view);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gyakszerk:
                createGyakorlatDialog(modelConverter.fromUI((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijeloltGyakPoz)));
                break;
            case R.id.gyaktorol:
                showAlertGyakTorles(modelConverter.fromUI((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijeloltGyakPoz)));
                break;
            case R.id.gyakszerk_menu_video:
                Gyakorlat gyakorlat = modelConverter.fromUI((GyakorlatUI) binding.gyakorlatokLista.getAdapter().getItem(kijeloltGyakPoz));
                if (gyakorlat != null && gyakorlat.getVideolink().length() > 0) {
                    Intent videointent = new Intent(this, VideoActivity.class);
                    videointent.putExtra(VideoActivity.EXTRA_GYAKORLAT, gyakorlat);
                    startActivity(videointent);
                } else {
                    Toast.makeText(this, "Nincs video!", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onContextItemSelected(item);
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
}