package aa.droid.norbo.projects.edzesnaplo3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.datainterfaces.AdatBeallitoInterface;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProvider;
import aa.droid.norbo.projects.edzesnaplo3.rcview.NaploAdapter;

public class JelenlegiEdzesFragment extends Fragment {
    private AdatBeallitoInterface adatBeallitoInterface;
    private String felhasznalonev;

    private RecyclerView rc;
    private TextView napi_osszsuly;
    private SorozatViewModel sorozatViewModel;
    private NaploViewModel naploViewModel;
    private Naplo naplo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.adatBeallitoInterface = (AdatBeallitoInterface) context;
        this.felhasznalonev = adatBeallitoInterface.getFelhasznaloNev();

        this.sorozatViewModel = new ViewModelProvider(this)
                .get(SorozatViewModel.class);
        this.naploViewModel = new ViewModelProvider(this)
                .get(NaploViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabbed_naplo, container, false);
        rc = view.findViewById(R.id.rcMentettNaploTabbed);
        //csak a rend kedvéért az alábbi két sor
        rc.setAdapter(new NaploAdapter(getContext(), new ArrayList<>()));
        rc.setLayoutManager(new LinearLayoutManager(getContext()));

        napi_osszsuly = view.findViewById(R.id.aktualnaplo_ossz_suly_tabbed);

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey("naplo")) {
                naplo = (Naplo) savedInstanceState.getSerializable("naplo");
                if(naplo != null) updateNaploAdat(naplo);
            }
        }

        view.findViewById(R.id.fabNaploMentesTabbed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(naplo == null) {
                    Toast.makeText(getContext(), "Naplo 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(naplo.getSorozats().size() > 0) {
                    naploViewModel.insert(naplo);
                    sorozatViewModel.insert(naplo.getSorozats());
                    Toast.makeText(getContext(), "Megtörtént a mentés", Toast.LENGTH_SHORT).show();
                    ((NaploAdapter) rc.getAdapter()).clear();
                    napi_osszsuly.setText(R.string.napi_osszsuly);

                    NaploContentProvider.sendRefreshBroadcast(getContext());
                } else {
                    Toast.makeText(getContext(), "Nem lehet mit menteni", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void updateNaploAdat(Naplo naplo) {
        this.naplo = naplo;
        NaploActivity naploActivity = new NaploActivity();
        List<Sorozat> sorozats = naplo.getSorozats();
        List<NaploActivity.RCViewGyakSorozat> withSorozats = naploActivity.doitGyakEsSorozat(sorozats);

        napi_osszsuly.setText(String.format(Locale.getDefault(),"%d Kg napi megmozgatott súly",
                naploActivity.getNapiOsszSuly(withSorozats)));

        rc.setAdapter(new NaploAdapter(getContext(), withSorozats));
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        rc.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("naplo", naplo);
        super.onSaveInstanceState(outState);
    }
}
