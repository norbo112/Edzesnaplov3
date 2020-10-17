package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmKorabbiSorozatItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.KorabbiSorozatRcViewAdapter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class SorozatUtil {
    private Context context;
    private DateTimeFormatter formatter;
    private SorozatViewModel sorozatViewModel;

    @Inject
    public SorozatUtil(@ActivityContext Context context, DateTimeFormatter formatter, SorozatViewModel sorozatViewModel) {
        this.context = context;
        this.formatter = formatter;
        this.sorozatViewModel = sorozatViewModel;
    }

    public void sorozatNezokeDialog(LifecycleOwner owner, GyakorlatUI gyakorlatUI) {
        if(gyakorlatUI == null) {
            Toast.makeText(context, "Kérlek válassz egy gyakorlatot a megtekintéshez!", Toast.LENGTH_SHORT).show();
            return;
        }

        sorozatViewModel.getSorozatByGyakorlat(gyakorlatUI.getId()).observe(owner, sorozats -> {
            if(sorozats != null && sorozats.size() > 0) {
                new AlertDialog.Builder(context)
                        .setTitle(gyakorlatUI.getMegnevezes())
                        .setAdapter(getSorozatEsNaploAdapter(sorozats), null)
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Toast.makeText(context, "Nincs rögzítve még sorozat evvel a gyakorlattal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayAdapter<NaploEsSorozat> getSorozatEsNaploAdapter(List<Sorozat> sorozats) {
        return new ArrayAdapter<NaploEsSorozat>(context, R.layout.mvvm_korabbi_sorozat_item, makeNaploEsSorozat(sorozats)) {
            MvvmKorabbiSorozatItemBinding itemBinding;

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.mvvm_korabbi_sorozat_item, parent, false);
                    itemBinding = DataBindingUtil.bind(convertView);
                    convertView.setTag(itemBinding);
                } else {
                    itemBinding = (MvvmKorabbiSorozatItemBinding) convertView.getTag();
                }

                NaploEsSorozat item = getItem(position);
                itemBinding.korabbiSorozatDatumLabel.setText(formatter.getNaploDatum(item.naplodatum) + " " + getSorozatOsszSuly(item.sorozats));
                itemBinding.korabbiSorozatLista.setAdapter(new KorabbiSorozatRcViewAdapter(item.sorozats, formatter));
//                itemBinding.korabbiSorozatLista.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                itemBinding.korabbiSorozatLista.setLayoutManager(new LinearLayoutManager(context));
                return itemBinding.getRoot();
            }
        };
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

    private class NaploEsSorozat {
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
