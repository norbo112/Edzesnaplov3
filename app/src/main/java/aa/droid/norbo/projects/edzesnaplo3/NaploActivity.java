package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProvider;
import aa.droid.norbo.projects.edzesnaplo3.rcview.NaploAdapter;

public class NaploActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private String felhasznalonev;
    private Naplo naplo;

    private NaploViewModel naploViewModel;
    private SorozatViewModel sorozatViewModel;
    private TextView aktual_napi_osszsuly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naplo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Napló");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initIntentExtraData(getIntent());

        aktual_napi_osszsuly = findViewById(R.id.aktualnaplo_ossz_suly);
        RecyclerView rc = findViewById(R.id.rcMentettNaplo);
        List<GyakorlatWithSorozat> sorozats = doitGyakEsSorozat(naplo.getSorozats());

        aktual_napi_osszsuly.setText(String.format(Locale.getDefault(),"%d Kg napi megmozgatott súly", getNapiOsszSuly(sorozats)));

        rc.setAdapter(new NaploAdapter(this, sorozats));
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setItemAnimator(new DefaultItemAnimator());

        naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        sorozatViewModel = new ViewModelProvider(this).get(SorozatViewModel.class);

        findViewById(R.id.fabNaploMentes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(naplo.getSorozats().size() > 0) {
                    naploViewModel.insert(naplo);
                    sorozatViewModel.insert(naplo.getSorozats());
                    Toast.makeText(NaploActivity.this, "Megtörtént a mentés", Toast.LENGTH_SHORT).show();
                    ((NaploAdapter) rc.getAdapter()).clear();

                    NaploContentProvider.sendRefreshBroadcast(NaploActivity.this);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(NaploActivity.this, "Nem lehet mit menteni", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initIntentExtraData(Intent intent) {
        felhasznalonev = intent.getStringExtra(MainActivity.INTENT_DATA_NEV);
        naplo = (Naplo) intent.getSerializableExtra(MainActivity.INTENT_DATA_NAPLO);
        //DEBUG
        Log.i(TAG, "initIntentExtraData: Naplo: "+naplo.getNaplodatum()+"\n"+
                "sorozat méret= "+naplo.getSorozats().size());
    }

    public List<GyakorlatWithSorozat> doitGyakEsSorozat(List<Sorozat> sorozats) {
        List<GyakorlatWithSorozat> withSorozats = new ArrayList<>();
        Set<Gyakorlat> egyediGyaknevek = new HashSet<>();
        for (int i = 0; i < sorozats.size(); i++) {
            egyediGyaknevek.add(sorozats.get(i).getGyakorlat());
        }

        List<Gyakorlat> tempEgyediNevek = new ArrayList<>(egyediGyaknevek);
        GyakorlatWithSorozat gyakorlatWithSorozat;

        for (int i = 0; i < tempEgyediNevek.size(); i++) {
            Gyakorlat gyakorlat = tempEgyediNevek.get(i);
            gyakorlatWithSorozat = new GyakorlatWithSorozat(gyakorlat);
            for (int j = 0; j < sorozats.size(); j++) {
                if(gyakorlat.getMegnevezes().equals(sorozats.get(j).getGyakorlat().getMegnevezes())) {
                    gyakorlatWithSorozat.addSorozat(sorozats.get(j));
                }
            }
            withSorozats.add(gyakorlatWithSorozat);
        }

        return withSorozats;
    }

    public List<GyakorlatWithSorozat> doitMentettNaploMegjelenesre(List<SorozatWithGyakorlat> sorozats) {
        List<GyakorlatWithSorozat> withSorozats = new ArrayList<>();
        Set<Gyakorlat> egyediGyaknevek = new HashSet<>();
        for (int i = 0; i < sorozats.size(); i++) {
            egyediGyaknevek.add(sorozats.get(i).gyakorlat);
        }

        List<Gyakorlat> tempEgyediNevek = new ArrayList<>(egyediGyaknevek);
        GyakorlatWithSorozat gyakorlatWithSorozat;

        for (int i = 0; i < tempEgyediNevek.size(); i++) {
            Gyakorlat gyakorlat = tempEgyediNevek.get(i);
            gyakorlatWithSorozat = new GyakorlatWithSorozat(gyakorlat);
            for (int j = 0; j < sorozats.size(); j++) {
                if(gyakorlat.getMegnevezes().equals(sorozats.get(j).gyakorlat.getMegnevezes())) {
                    gyakorlatWithSorozat.addSorozat(sorozats.get(j).sorozat);
                }
            }
            withSorozats.add(gyakorlatWithSorozat);
        }

        return withSorozats;
    }

    private int getNapiOsszSuly(List<GyakorlatWithSorozat> withSorozats) {
        int result = 0;
        for (int i = 0; i < withSorozats.size(); i++) {
            result += withSorozats.get(i).getMegmozgatottSuly();
        }
        return result;
    }

    public static class GyakorlatWithSorozat {
        private Gyakorlat gyakorlat;
        private List<Sorozat> sorozatList;
        private int megmozgatottSuly;
        private int elteltido = 0;

        GyakorlatWithSorozat(Gyakorlat gyakorlat) {
            this.gyakorlat = gyakorlat;
            this.sorozatList = new ArrayList<>();
        }

        public Gyakorlat getGyakorlat() {
            return gyakorlat;
        }

        public List<Sorozat> getSorozatList() {
            return sorozatList;
        }

        public int getMegmozgatottSuly() {
            return megmozgatottSuly;
        }

        public int getElteltido() {
            return elteltido;
        }

        void addSorozat(Sorozat sorozat) {
            sorozatList.add(sorozat);
            megmozgatottSuly += sorozat.getSuly() * sorozat.getIsmetles();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                elteltido = getEltelIdoSzamitas();
            } else {
                elteltido = 0;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private int getEltelIdoSzamitas() {
            int result;
            sorozatList.sort(new Comparator<Sorozat>() {
                @Override
                public int compare(Sorozat o1, Sorozat o2) {
                    int one = new Date(o1.getIsmidopont()).getMinutes();
                    int two = new Date(o2.getIsmidopont()).getMinutes();
                    return Integer.compare(two,one);
                }
            });
            result = new Date(sorozatList.get(0).getIsmidopont()).getMinutes();
            for (int i = 1; i < sorozatList.size(); i++) {
                result -= new Date(sorozatList.get(i).getIsmidopont()).getMinutes();
            }
            return result;
        }
    }
}
