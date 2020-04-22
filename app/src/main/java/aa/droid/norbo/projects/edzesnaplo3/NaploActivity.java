package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProvider;
import aa.droid.norbo.projects.edzesnaplo3.rcview.NaploAdapter;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.lists.SorozatSorter;

public class NaploActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private String felhasznalonev;
    private Naplo naplo;

    private NaploViewModel naploViewModel;
    private SorozatViewModel sorozatViewModel;
    private TextView aktual_napi_osszsuly;

    private boolean record = false;
    private JelenlegiEdzesFragment edzesFragment;

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
        List<RCViewGyakSorozat> sorozats = doitGyakEsSorozat(naplo.getSorozats());

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

        findViewById(R.id.fabNaploCommentSmall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edzesFragment == null) edzesFragment = new JelenlegiEdzesFragment();
                if(!edzesFragment.isRecordison()) {
                    edzesFragment.audiorecord(getApplicationContext(), getSharedPreferences("naplo",MODE_PRIVATE), naplo);
                } else {
                    edzesFragment.stopRecord();
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

    public List<RCViewGyakSorozat> doitGyakEsSorozat(List<Sorozat> sorozats) {
        List<RCViewGyakSorozat> withSorozats = new ArrayList<>();
        Set<Gyakorlat> egyediGyaknevek = new HashSet<>();
        for (int i = 0; i < sorozats.size(); i++) {
            egyediGyaknevek.add(sorozats.get(i).getGyakorlat());
        }

        List<Gyakorlat> tempEgyediNevek = new ArrayList<>(egyediGyaknevek);
        RCViewGyakSorozat gyakorlatWithSorozat;

        for (int i = 0; i < tempEgyediNevek.size(); i++) {
            Gyakorlat gyakorlat = tempEgyediNevek.get(i);
            gyakorlatWithSorozat = new RCViewGyakSorozat(gyakorlat);
            for (int j = 0; j < sorozats.size(); j++) {
                if(gyakorlat.getMegnevezes().equals(sorozats.get(j).getGyakorlat().getMegnevezes())) {
                    gyakorlatWithSorozat.addSorozat(sorozats.get(j));
                }
            }
            withSorozats.add(gyakorlatWithSorozat);
        }

        return withSorozats;
    }

    public List<RCViewGyakSorozat> doitMentettNaploMegjelenesre(List<SorozatWithGyakorlat> sorozats) {
        List<RCViewGyakSorozat> withSorozats = new ArrayList<>();
        Set<Gyakorlat> egyediGyaknevek = new HashSet<>();
        for (int i = 0; i < sorozats.size(); i++) {
            egyediGyaknevek.add(sorozats.get(i).gyakorlat);
        }

        List<Gyakorlat> tempEgyediNevek = new ArrayList<>(egyediGyaknevek);
        RCViewGyakSorozat gyakorlatWithSorozat;

        for (int i = 0; i < tempEgyediNevek.size(); i++) {
            Gyakorlat gyakorlat = tempEgyediNevek.get(i);
            gyakorlatWithSorozat = new RCViewGyakSorozat(gyakorlat);
            for (int j = 0; j < sorozats.size(); j++) {
                if(gyakorlat.getMegnevezes().equals(sorozats.get(j).gyakorlat.getMegnevezes())) {
                    gyakorlatWithSorozat.addSorozat(sorozats.get(j).sorozat);
                }
            }
            withSorozats.add(gyakorlatWithSorozat);
        }

        SorozatSorter.rcGyakSorozatSort(withSorozats);
        return withSorozats;
    }

    public int getNapiOsszSuly(List<RCViewGyakSorozat> withSorozats) {
        int result = 0;
        for (int i = 0; i < withSorozats.size(); i++) {
            result += withSorozats.get(i).getMegmozgatottSuly();
        }
        return result;
    }

    public static class RCViewGyakSorozat {
        private Gyakorlat gyakorlat;
        private List<Sorozat> sorozatList;
        private int megmozgatottSuly;
        private int elteltido = 0;
        private String TAG = "GyakorlatWithSorozat";

        RCViewGyakSorozat(Gyakorlat gyakorlat) {
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
            elteltido = getEltelIdoSzamitas();
        }

        private int getEltelIdoSzamitas() {
            int result;
            try {
                result = new Date(
                        Long.parseLong(sorozatList.get(sorozatList.size()-1).getIsmidopont())).getMinutes();
                result -= new Date(
                        Long.parseLong(sorozatList.get(0).getIsmidopont())).getMinutes();
            } catch (IllegalArgumentException ex) {
                Log.i(TAG, "getEltelIdoSzamitas: Dátum parse");
                result = 0;
            }
            return Math.abs(result);
        }

        @NonNull
        @Override
        public String toString() {
            return "{ "+gyakorlat.getMegnevezes()+" "+sorozatList+" }";
        }
    }
}
