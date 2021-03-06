package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

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
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.VideoUtils;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatValasztoFragment extends Fragment implements AdapterView.OnItemClickListener,
        EdzesTervManageUtil.TervValasztoInterface, YouTubePlayer.OnInitializedListener {
    private static final String TAG = "TestActivity";
    private static final String VALASSZ_IZOMCSOP = "Válassz...";
    private MvvmGyakorlatValasztoLayoutBinding binding;
    private AdatKozloInterface adatKozloInterface;
    private boolean gyakorlatValasztva;

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerSupportFragment youtubePlayerFragment;

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

        if (isTablet()) {
            youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.ytView, youtubePlayerFragment).commit();
            youtubePlayerFragment.initialize(VideoUtils.YT_API_KEY, this);
        }

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
        GyakorlatUI valasztottGyakorlat = (GyakorlatUI) binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz);
        switch (item.getItemId()) {
            case R.id.gyakszerk_menu_select :
                if(!gyakorlatValasztva) {
                    adatKozloInterface.gyakorlatAtado(valasztottGyakorlat);
                    gyakorlatValasztva = true;

                    if (isTablet() && youtubePlayerFragment != null && youTubePlayer != null) {
                        startVideo(valasztottGyakorlat);
                    }
                    Toast.makeText(getContext(), "Gyakorlat kiválasztva", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Gyakorlat választása az edzés NEW gombbal történik!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gyakszerk_menu_korabbisorozat :
                sorozatUtil.sorozatNezokeDialog(this, valasztottGyakorlat);
                break;
            case R.id.gyakszerk_menu_video :
                Gyakorlat gyakorlat = modelConverter.fromUI(valasztottGyakorlat);
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

    public void setGyakorlatValasztva(boolean gyakorlatValasztva) {
        this.gyakorlatValasztva = gyakorlatValasztva;
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            this.youTubePlayer = youTubePlayer;
            GyakorlatUI gyakorlatUI = (GyakorlatUI) binding.lvGyakorlat.getAdapter().getItem(kijeloltGyakPoz);
            startVideo(gyakorlatUI);
        }
    }

    public void startVideo(GyakorlatUI gyakorlatUI) {
        if(gyakorlatUI.getVideolink() != null && gyakorlatUI.getVideolink().length() > 2) {
            youTubePlayer.cueVideo(gyakorlatUI.getVideolink(), Integer.parseInt(gyakorlatUI.getVideostartpoz()) * 1000);
            binding.ytView.setVisibility(View.VISIBLE);
            binding.videoWarningText.setVisibility(View.GONE);
        } else {
            binding.videoWarningText.setVisibility(View.VISIBLE);
            binding.ytView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.i(TAG, "onInitializationFailure: Failed youtube initialize: "+youTubeInitializationResult.toString());
    }
}
