package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzesNezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.adatkozlo.AdatKozloInterface;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.NaploWorker;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegFragment extends Fragment implements AdatKozloInterface {
    private MvvmEdzesNezetBinding binding;
    private GyakorlatUI gyakorlatUI;

    @Inject
    NaploWorker naploWorker;

    @Inject
    ModelConverter modelConverter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmEdzesNezetBinding.inflate(inflater, container, false);
        binding.setSorozatUI(new SorozatUI());
        binding.setAction(new TevekenysegClick());

        naploWorker.getLiveSorozatLista().observe(getViewLifecycleOwner(), sorozats -> {
            if(sorozats != null) {
                binding.sorozatLista.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, sorozats));
                binding.sorozatLabel.setText(naploWorker.getSorozatOsszSuly()+" Kg");
            }
        });

        return binding.getRoot();
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
            SorozatUI sorozatUI = binding.getSorozatUI();
            naploWorker.addSorozat(Integer.parseInt(sorozatUI.suly), Integer.parseInt(sorozatUI.ism));
            Toast.makeText(getContext(), "(* *)", Toast.LENGTH_SHORT).show();
        }

        public void naploSave() {
            new AlertDialog.Builder(getContext())
                    .setTitle("Napló részletek (mentés)")
                    .setMessage(naploWorker.getNaplo().toString())
                    .setNeutralButton("ok", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("mentés", (dialog, which) -> {
                        naploWorker.saveNaplo();
                        Toast.makeText(getContext(), "Napló mentve!", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }

        public void ujGyakorlat() {
            ((ViewPager)getActivity().findViewById(R.id.view_pager)).setCurrentItem(0, true);
            binding.etSuly.setText("");
            binding.etIsm.setText("");
            binding.btnSorozatAdd.setEnabled(false);
            naploWorker.prepareUjGyakorlat();
        }
    }

    public class SorozatUI {
        String suly;
        String ism;

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
}
