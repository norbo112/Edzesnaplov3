package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzesNezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmSorozatSzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.SorozatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegFragment extends Fragment implements AdatKozloInterface {
    private static final String TAG = "TevekenysegFragment";
    private MvvmEdzesNezetBinding binding;
    private GyakorlatUI gyakorlatUI;

    private Handler handler = new Handler();
    private TimerRunner timerRunner;

    @Inject
    NaploWorker naploWorker;

    @Inject
    ModelConverter modelConverter;

    @Inject
    DateTimeFormatter formatter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmEdzesNezetBinding.inflate(inflater, container, false);
        binding.setSorozatUI(new SorozatDisplay());
        binding.setAction(new TevekenysegClick());
        binding.setSorozatAction(new SorozatAction());
        binding.btnSorozatAdd.setEnabled(false);

        binding.sorozatLista.setNestedScrollingEnabled(true);

        naploWorker.getLiveSorozatLista().observe(getViewLifecycleOwner(), sorozats -> {
            if(sorozats != null) {
                binding.sorozatLista.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, sorozats));
                sorozatSzerkesztese(binding.sorozatLista);
                binding.sorozatLabel.setText(naploWorker.getSorozatOsszSuly()+" Kg");
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
                        if(TextUtils.isEmpty(sorozatSzerkesztoBinding.sorozatSuly.getText().toString()) ||
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
        binding.gyakTitle.setText(gyakorlatUI.getMegnevezes()+" használata");
        this.gyakorlatUI = gyakorlatUI;
        naploWorker.setGyakorlat(modelConverter.fromUI(gyakorlatUI));
        binding.btnSorozatAdd.setEnabled(true);
    }

    public class TevekenysegClick {
        public void addSorozat() {
            handler.removeCallbacks(timerRunner);
            binding.tvStopper.setText("00:00");

            SorozatDisplay sorozatUI = binding.getSorozatUI();
            if(sorozatUI.suly != null && sorozatUI.ism != null) {
                naploWorker.addSorozat(Integer.parseInt(sorozatUI.suly), Integer.parseInt(sorozatUI.ism));

                timerRunner = new TimerRunner();
                handler.postDelayed(timerRunner, 500);
                Toast.makeText(getContext(), "(* *)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Súly vagy ismétlés nem lehet üres!", Toast.LENGTH_SHORT).show();
            }
        }

        public void ujGyakorlat() {
            if (! getActivity().getResources().getBoolean(R.bool.isTablet)) {
                ((ViewPager)getActivity().findViewById(R.id.view_pager)).setCurrentItem(0, true);
            }
            binding.etSuly.setText("0");
            binding.etIsm.setText("0");
            binding.btnSorozatAdd.setEnabled(false);
            handler.removeCallbacks(timerRunner);
            binding.tvStopper.setText("00:00");

            naploWorker.prepareUjGyakorlat();
        }
    }

    public class SorozatAction {
        public void increaseSorozatSuly(SorozatDisplay sorozatUI) {
            int suly = Integer.parseInt(sorozatUI.getSuly());
            suly += 2;
            sorozatUI.setSuly(suly+"");
            binding.setSorozatUI(sorozatUI);
        }

        public void decreaseSorozatSuly(SorozatDisplay sorozatUI) {
            int suly = Integer.parseInt(sorozatUI.getSuly());
            suly -= 2;
            if(suly < 0) suly = 0;
            sorozatUI.setSuly(suly+"");
            binding.setSorozatUI(sorozatUI);
        }

        public void increaseSorozatIsm(SorozatDisplay sorozatUI) {
            int ism = Integer.parseInt(sorozatUI.getIsm());
            ism += 2;
            sorozatUI.setIsm(ism+"");
            binding.setSorozatUI(sorozatUI);
        }

        public void decreaseSorozatIsm(SorozatDisplay sorozatUI) {
            int ism = Integer.parseInt(sorozatUI.getIsm());
            ism -= 2;
            if(ism < 0) ism = 0;
            sorozatUI.setIsm(ism+"");
            binding.setSorozatUI(sorozatUI);
        }
    }

    public class SorozatDisplay {
        String suly = "0";
        String ism = "0";

        public String getSuly() {
            return suly;
        }

        public void setSuly(String suly) {
            this.suly = suly;
        }

        public String getIsm() {
            return ism;
        }

        public void setIsm(String ism) {
            this.ism = ism;
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

            if(masodperc == 60) {
                perc++;
                masodperc = 0;
            }

            if(perc == 60) perc = 0;

            binding.tvStopper.setText(getOo(perc)+":"+getOo(masodperc));
            handler.postDelayed(this, 1000);
        }

        private String getOo(int szam) {
            if (szam / 10 < 1) return "0" + szam;
            else return "" + szam;
        }
    }
}
