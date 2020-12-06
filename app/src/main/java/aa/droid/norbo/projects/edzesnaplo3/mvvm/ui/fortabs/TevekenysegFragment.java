package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzesNezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmSorozatSzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.SzuperszettSorozatRogzitoLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.SorozatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.szuperszett.SzettekSzamozas;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions.MainTevekenysegAction;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions.SorozatAction;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions.SorozatRogzitoAction;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions.SzettGyakRemover;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions.TevekenysegSzuperszettSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegFragment extends Fragment implements AdatKozloInterface, SzettGyakRemover {
    private static final String TAG = "TevekenysegFragment";
    private MvvmEdzesNezetBinding binding;
    private GyakorlatUI gyakorlatUI;
    private final List<SzuperszettSorozatRogzitoLayoutBinding> sorozatRogzitoLayoutBindings = new ArrayList<>();

    private Handler handler = new Handler();
    private TimerRunner timerRunner;

    @Inject
    NaploWorker naploWorker;

    @Inject
    ModelConverter modelConverter;

    @Inject
    DateTimeFormatter formatter;

    @Inject
    SorozatUtil sorozatUtil;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    SzettekSzamozas szettekSzamozas;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmEdzesNezetBinding.inflate(inflater, container, false);
        binding.setSorozatUI(new SorozatDisplay());
        binding.setAction(new TevekenysegClick());
        binding.btnSorozatAdd.setEnabled(false);
        binding.setSorozatAction(new SorozatAction(new MainTevekenysegAction(binding)));
        binding.sorozatLista.setNestedScrollingEnabled(true);

        if (getResources().getBoolean(R.bool.isTablet))
            binding.setSorozatActionForTab2x(new SorozatActionForTab2x());

        naploWorker.getLiveSorozatLista().observe(getViewLifecycleOwner(), sorozats -> {
            if (sorozats != null) {
                sorozats.sort((o1, o2) -> Long.compare(o2.getIsmidopont(), o1.getIsmidopont()));
                binding.sorozatLista.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, sorozats));
                sorozatSzerkesztese(binding.sorozatLista);
                binding.sorozatLabel.setText(naploWorker.getSorozatOsszSuly() + " Kg");
            }
        });

        return binding.getRoot();
    }

    private void sorozatSzerkesztese(ListView sorozatLista) {
        sorozatLista.setOnItemClickListener((parent, view, position, id) -> {
            MvvmSorozatSzerkesztoBinding sorozatSzerkesztoBinding = MvvmSorozatSzerkesztoBinding.inflate(LayoutInflater.from(getContext()),
                    null, false);

            Sorozat sorozatItem = (Sorozat) parent.getAdapter().getItem(position);
            sorozatSzerkesztoBinding.setSorozat(modelConverter.fromSorozatEntity(sorozatItem));
            new AlertDialog.Builder(getContext())
                    .setTitle("Sorozat szerkesztése")
                    .setView(sorozatSzerkesztoBinding.getRoot())
                    .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("mentés", (dialog, which) -> {
                        if (TextUtils.isEmpty(sorozatSzerkesztoBinding.sorozatSuly.getText().toString()) ||
                                TextUtils.isEmpty(sorozatSzerkesztoBinding.sorozatIsm.getText().toString())) {
                            Toast.makeText(getContext(), "Kérlek töltsd ki a mezőket", Toast.LENGTH_SHORT).show();
                        } else {
                            SorozatUI sorozatUI = sorozatSzerkesztoBinding.getSorozat();
                            sorozatItem.setSuly(Integer.parseInt(sorozatUI.getSuly()));
                            sorozatItem.setIsmetles(Integer.parseInt(sorozatUI.getIsmetles()));
                            naploWorker.setSorozat(position, sorozatItem);
                            Toast.makeText(getContext(), "Sorozat frissítve", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void gyakorlatAtado(GyakorlatUI gyakorlatUI) {
        this.gyakorlatUI = gyakorlatUI;
        binding.gyakTitle.setText(gyakorlatUI.getMegnevezes() + " használata");
        binding.btnSorozatAdd.setEnabled(true);

        if (getResources().getBoolean(R.bool.isTablet)) {
            sorozatViewModel.getSorozatByGyakorlat(gyakorlatUI.getId()).observe(this, sorozats -> {
                if (sorozats != null && sorozats.size() > 0) {
                    binding.korabbanElvegzettSorozatok.setAdapter(sorozatUtil.getSorozatEsNaploAdapter(sorozats));
                    binding.tvSorozatKorabbiTitle.setText(R.string.korabbi_sorozat_list_title);
                } else {
                    binding.tvSorozatKorabbiTitle.setText("Még nem rögzítettél sorozatot evvel a gyakorlattal!");
                }
            });
        }
    }

    private int szuperSzettViewCount = 0;

    @Override
    public void pluszGyakorlatFelvetele(GyakorlatUI gyakorlatUI) {
        binding.sztrszettLabel.setVisibility(View.VISIBLE);
        SzuperszettSorozatRogzitoLayoutBinding layoutBinding = SzuperszettSorozatRogzitoLayoutBinding.inflate(getLayoutInflater());
        layoutBinding.setAction(new TevekenysegSzuperszettSorozat(layoutBinding, this));
        layoutBinding.setSorozatAction(new SorozatAction(new SorozatRogzitoAction(layoutBinding)));
        layoutBinding.setGyakorlatUI(gyakorlatUI);
        layoutBinding.setSorozatUI(new SorozatDisplay());
        layoutBinding.setViewCnt(szuperSzettViewCount++);
        sorozatRogzitoLayoutBindings.add(layoutBinding);
        this.binding.szuperszettSorozatFelvetele.addView(layoutBinding.getRoot());
    }

    private void korabbiSorozatReset() {
        if (getResources().getBoolean(R.bool.isTablet)) {
            binding.tvSorozatKorabbiTitle.setText(R.string.korabbi_sorozat_list_title);
            ArrayAdapter adapter = (ArrayAdapter) binding.korabbanElvegzettSorozatok.getAdapter();
            if (adapter != null)
                adapter.clear();
        }
    }

    @Override
    public void removeSzettGyakById(int index) {
        binding.szuperszettSorozatFelvetele.removeViewAt(index);
        sorozatRogzitoLayoutBindings.remove(index);
        if (binding.szuperszettSorozatFelvetele.getChildCount() < 1) {
            binding.sztrszettLabel.setVisibility(View.GONE);
            szuperSzettViewCount = 0;
        }
    }

    public class TevekenysegClick {
        public void addSorozat() {
            handler.removeCallbacks(timerRunner);
            binding.tvStopper.setText("00:00");

            SorozatDisplay sorozatUI = binding.getSorozatUI();
            if (!sorozatUI.suly.equals("0") && !sorozatUI.ism.equals("0") && szettekBevitele()) {
                String szettJelolo = getSzettJelolo();
                naploWorker.addSorozat(modelConverter.fromUI(gyakorlatUI), Integer.parseInt(sorozatUI.suly), Integer.parseInt(sorozatUI.ism), szettJelolo);

                if (sorozatRogzitoLayoutBindings.size() > 0) {
                    for (SzuperszettSorozatRogzitoLayoutBinding binding : sorozatRogzitoLayoutBindings) {
                        SorozatDisplay sorozatUI1 = binding.getSorozatUI();
                        naploWorker.addSorozat(modelConverter.fromUI(binding.getGyakorlatUI()), Integer.parseInt(sorozatUI1.suly),
                                Integer.parseInt(sorozatUI1.ism), szettJelolo);
                        binding.szettSorozatRemoveBtn.setEnabled(false); //ha már hozzáadtál egy sorozatot, akkor ne legyen eltávolítható
                    }
                }

                timerRunner = new TimerRunner();
                handler.postDelayed(timerRunner, 500);
                Toast.makeText(getContext(), "(* *)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Súly vagy ismétlés nem lehet 0!", Toast.LENGTH_SHORT).show();
            }
        }

        public void ujGyakorlat() {
            if (!getActivity().getResources().getBoolean(R.bool.isTablet)) {
                ((ViewPager) getActivity().findViewById(R.id.view_pager)).setCurrentItem(0, true);
            }

            binding.szuperszettSorozatFelvetele.removeAllViews();
            binding.sztrszettLabel.setVisibility(View.GONE);
            sorozatRogzitoLayoutBindings.clear();
            szuperSzettViewCount = 0;

            binding.gyakTitle.setText(R.string.mvvm_edzes_nezet_gyakorlat_label);
            binding.etSuly.setText("0");
            binding.etIsm.setText("0");
            binding.btnSorozatAdd.setEnabled(false);
            handler.removeCallbacks(timerRunner);
            binding.tvStopper.setText("00:00");

            naploWorker.prepareUjGyakorlat();

            gyakorlatValasztastAllit();
            korabbiSorozatReset();
        }

        private boolean szettekBevitele() {
            if (sorozatRogzitoLayoutBindings.size() == 0) return true;
            for (SzuperszettSorozatRogzitoLayoutBinding binding : sorozatRogzitoLayoutBindings) {
                SorozatDisplay sorozatUI = binding.getSorozatUI();
                return !sorozatUI.suly.equals("0") && !sorozatUI.ism.equals("0");
            }
            return false;
        }
    }

    private String getSzettJelolo() {

        int szettBindingsSize = sorozatRogzitoLayoutBindings.size();

        if (szettBindingsSize < 1) return null;

        List<String> jelolesek = naploWorker.getNaplo().getSorozats().stream().map(Sorozat::getSzettek).collect(Collectors.toList());

        switch (szettBindingsSize) {
            case 1:
                return SzettekSzamozas.SZUPERSZETT + Integer.toString(szettekSzamozas.getSzuperszettSzamozas(jelolesek));
            case 2:
                return SzettekSzamozas.TRISZETT + Integer.toString(szettekSzamozas.getTriszettSzamozas(jelolesek));
            case 3:
                return SzettekSzamozas.ORIASSZETT + Integer.toString(szettekSzamozas.getOriasSzamozas(jelolesek));
            default:
                return null;
        }
    }

    private void gyakorlatValasztastAllit() {
        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MvvmGyakorlatValasztoFragment) {
                ((MvvmGyakorlatValasztoFragment) fragment).setGyakorlatValasztva(false);
            }
        }
    }

    public class SorozatActionForTab2x {
        public void increaseSorozatSuly2x(SorozatDisplay sorozatUI) {
            int suly = Integer.parseInt(sorozatUI.getSuly());
            suly += 10;
            binding.etSuly.setText(Integer.toString(suly));
        }

        public void decreaseSorozatSuly2x(SorozatDisplay sorozatUI) {
            int suly = Integer.parseInt(sorozatUI.getSuly());
            suly -= 10;
            if (suly < 0) suly = 0;
            binding.etSuly.setText(Integer.toString(suly));
        }

        public void increaseSorozatIsm2x(SorozatDisplay sorozatUI) {
            int ism = Integer.parseInt(sorozatUI.getIsm());
            ism += 10;
            binding.etIsm.setText(Integer.toString(ism));
        }

        public void decreaseSorozatIsm2x(SorozatDisplay sorozatUI) {
            int ism = Integer.parseInt(sorozatUI.getIsm());
            ism -= 10;
            if (ism < 0) ism = 0;
            binding.etIsm.setText(Integer.toString(ism));
        }
    }

    private class TimerRunner implements Runnable {
        int perc;
        int masodperc;

        public TimerRunner() {
            this.perc = 0;
            this.masodperc = 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            masodperc++;

            if (masodperc == 60) {
                perc++;
                masodperc = 0;
            }

            if (perc == 60) perc = 0;

            binding.tvStopper.setText(getOo(perc) + ":" + getOo(masodperc));
            handler.postDelayed(this, 1000);
        }

        private String getOo(int szam) {
            if (szam / 10 < 1) return "0" + szam;
            else return "" + szam;
        }
    }
}
