package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatValasztoLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakorlatdialogBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.VideoActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervManageUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters.GyakorlatItemAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatValasztoFragment extends Fragment implements AdapterView.OnItemClickListener, EdzesTervManageUtil.TervValasztoInterface {
    private static final String TAG = "TestActivity";
    private static final String VALASSZ_IZOMCSOP = "Válassz...";
    private MvvmGyakorlatValasztoLayoutBinding binding;
    private AdatKozloInterface adatKozloInterface;

    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    @Inject
    ModelConverter modelConverter;

    @Inject
    SorozatUtil sorozatUtil;

    @Inject
    EdzesTervManageUtil manageUtil;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.adatKozloInterface = (AdatKozloInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmGyakorlatValasztoLayoutBinding.inflate(inflater, container, false);

        gyakorlatViewModel.getGyakorlatList().observe(getViewLifecycleOwner(), gyakorlats -> {
            if(gyakorlats != null) {
                binding.lvGyakorlat.setAdapter(
                        new GyakorlatItemAdapter(gyakorlats.stream().map(gy -> modelConverter.fromEntity(gy)).collect(Collectors.toList()), getContext()));
                binding.lvGyakorlat.setOnItemClickListener(this);
                binding.lvGyakorlat.setNestedScrollingEnabled(true);
                initIzomcsoportSpinner(gyakorlats, binding.spinIzomcsop);
                initSearch(binding.gyakSearchView);
            }
        });

        binding.fabone.setOnClickListener(v -> createGyakorlatDialog(null));

        return binding.getRoot();
    }

    private void initSearch(SearchView searchView) {
        searchView.setOnClickListener(v -> {
            if(binding.spinIzomcsop.getSelectedItemPosition() != 0) {
                binding.spinIzomcsop.setSelection(0);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ((GyakorlatItemAdapter) binding.lvGyakorlat.getAdapter()).getFilter().filter(newText);
                } catch (NullPointerException ex) {
                    Log.i(TAG, "onQueryTextChange: Valamiért null van itt amikor elforgatom a kijelzőt");
                }
                return false;
            }
        });
    }

    int kijeloltGyakPoz;

    @SuppressLint("RestrictedApi")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        kijeloltGyakPoz = position;

        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.gyak_szerkeszto_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(getContext(), (MenuBuilder) popupMenu.getMenu(), view);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gyakszerk_menu_select :
                adatKozloInterface.gyakorlatAtado((GyakorlatUI) binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz));
                Toast.makeText(getContext(), "Gyakorlat kiválasztva", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gyakszerk :
                createGyakorlatDialog(modelConverter.fromUI((GyakorlatUI)binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz)));
                break;
            case R.id.gyaktorol :
                showAlertGyakTorles(modelConverter.fromUI((GyakorlatUI)binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz)));
                break;
            case R.id.gyakszerk_menu_korabbisorozat :
                sorozatUtil.sorozatNezokeDialog(this, (GyakorlatUI) binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz));
                break;
            case R.id.gyakszerk_menu_video :
                Gyakorlat gyakorlat = modelConverter.fromUI((GyakorlatUI) binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz));
                if(gyakorlat != null && gyakorlat.getVideolink().length() > 0) {
                    Intent videointent = new Intent(getContext(), VideoActivity.class);
                    videointent.putExtra(VideoActivity.EXTRA_GYAKORLAT, gyakorlat);
                    startActivity(videointent);
                } else {
                    Toast.makeText(getContext(), "Nincs video!", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onContextItemSelected(item);
    }

    private void showAlertGyakTorles(Gyakorlat gyakorlat) {
        new AlertDialog.Builder(getContext())
                .setMessage("Biztos törölni akarod?")
                .setTitle(gyakorlat.getMegnevezes()+" törlése")
                .setPositiveButton("Igen", (dialog, which) -> {
                    gyakorlatViewModel.delete(gyakorlat);
                    Toast.makeText(getContext(), gyakorlat.getMegnevezes()+" törlésre került", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Nem", (dialog, which) -> {
                })
                .create()
                .show();
    }

    private void createGyakorlatDialog(Gyakorlat gyakorlat) {
        MvvmGyakorlatdialogBinding gyakBinding = DataBindingUtil.inflate(
                getLayoutInflater(), R.layout.mvvm_gyakorlatdialog, null, false);
        String title = (gyakorlat != null) ? gyakorlat.getMegnevezes()+" szerkesztése" : "Új gyakorlat felvétele";
        String[] izomccsoportResource = getResources().getStringArray(R.array.izomcsoportok);
        if(gyakorlat != null) {
            int szerkeszIndex = 0;
            for (int i=0; i<izomccsoportResource.length; i++) {
                if(izomccsoportResource[i].equals(gyakorlat.getCsoport())) {
                    szerkeszIndex = i;
                    break;
                }
            }
            gyakBinding.etGyakDialogCsoport.setSelection(szerkeszIndex);
            gyakBinding.setGyakorlat(modelConverter.fromEntity(gyakorlat));
        } else {
            gyakBinding.setGyakorlat(new GyakorlatUI());
        }

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(gyakBinding.getRoot())
                .setPositiveButton("OK", (dialog, which) -> {
                    String valasztottCsoport = gyakBinding.etGyakDialogCsoport.getSelectedItem().toString();

                    if(TextUtils.isEmpty(gyakBinding.etGyakDialogNev.getText().toString()) ||
                            valasztottCsoport.equals("Kérlek, válassz...")) {
                        Toast.makeText(getContext(), "Izomcsoport, megnevezés kötelező megadni", Toast.LENGTH_LONG).show();
                        return;
                    }

                    GyakorlatUI gyakorlat1 = gyakBinding.getGyakorlat();

                    setGyakorlatAdat(gyakBinding, gyakorlat1);

                    if(gyakorlat == null) {
                        gyakorlat1.setCsoport(valasztottCsoport);
                        gyakorlatViewModel.insert(modelConverter.fromUI(gyakorlat1));
                        Toast.makeText(getContext(), "Gyakorlat felvéve a listára", Toast.LENGTH_SHORT).show();
                    } else {
                        gyakorlatViewModel.update(modelConverter.fromUI(gyakorlat1));
                        Toast.makeText(getContext(), "Gyakorlat szerkesztve", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Mégse", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void setGyakorlatAdat(MvvmGyakorlatdialogBinding gyakBinding, GyakorlatUI gyakorlat1) {
        if(TextUtils.isEmpty(gyakBinding.etGyakDialogLeiras.getText().toString()))
            gyakorlat1.setLeiras("");

        if(TextUtils.isEmpty(gyakBinding.etGyakDialogVideolink.getText().toString()))
            gyakorlat1.setVideolink("");

        if(TextUtils.isEmpty(gyakBinding.etGyakDialogVideoStartPoz.getText().toString()))
            gyakorlat1.setVideostartpoz("0");
    }

    private void initIzomcsoportSpinner(List<Gyakorlat> gyakorlats, Spinner spinner) {
        List<String> izomcsoportList = new ArrayList<>();
        izomcsoportList.add(VALASSZ_IZOMCSOP);
        izomcsoportList.addAll(gyakorlats.stream().map(Gyakorlat::getCsoport).distinct().collect(Collectors.toList()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, izomcsoportList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Filter filter = ((GyakorlatItemAdapter) binding.lvGyakorlat.getAdapter()).getFilter();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void tervValasztva(EdzesTerv edzesTerv) {
        binding.tervMegnevezes.setText(edzesTerv.getMegnevezes()+" terv kiválasztva");
        binding.tervMegnevezes.setOnClickListener(v -> manageUtil.viewEdzesTervDialog(edzesTerv));
    }
}
