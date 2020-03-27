package aa.droid.norbo.projects.edzesnaplo3;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.rcview.NaploAdapter;

public class MentettNaploActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    private final String TAG = getClass().getSimpleName();

    private Spinner spinner;
    private RecyclerView rcnaploview;
    private TextView osszsnapisuly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentettnaplo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mentett Naplók");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcnaploview = findViewById(R.id.rcNaploMegtekinto);
        spinner = findViewById(R.id.lvMentettNaploLista);
        osszsnapisuly = findViewById(R.id.tvMOsszsuly);

        NaploViewModel naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        naploViewModel.getNaploListLiveData().observe(this, new Observer<List<Naplo>>() {
            @Override
            public void onChanged(List<Naplo> naplos) {
                ArrayAdapter adapter = new ArrayAdapter<>(MentettNaploActivity.this, android.R.layout.simple_spinner_item, naplos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(MentettNaploActivity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Naplo naplo = (Naplo) spinner.getSelectedItem();
        if(naplo != null) {
            SorozatViewModel sorozatViewModel = new ViewModelProvider(MentettNaploActivity.this)
                    .get(SorozatViewModel.class);
            CompletableFuture<LiveData<List<SorozatWithGyakorlat>>> sorozatWithGyakByNaplo =
                    sorozatViewModel.getSorozatWithGyakByNaplo(naplo.getNaplodatum());
            try {
                sorozatWithGyakByNaplo.get().observe(MentettNaploActivity.this, new Observer<List<SorozatWithGyakorlat>>() {
                    @Override
                    public void onChanged(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
                        List<NaploActivity.GyakorlatWithSorozat> withSorozats = new NaploActivity().doitMentettNaploMegjelenesre(sorozatWithGyakorlats);
                        int napiosszsuly = getNapiOsszSuly(withSorozats);
                        osszsnapisuly.setText(String.format(Locale.getDefault(),"%d Kg napi megmozgatott súly", napiosszsuly));
                        rcnaploview.setAdapter(new NaploAdapter(MentettNaploActivity.this, withSorozats));
                        rcnaploview.setLayoutManager(new LinearLayoutManager(MentettNaploActivity.this));
                        rcnaploview.setItemAnimator(new DefaultItemAnimator());
                        System.out.println("onChanged lefutott: " + withSorozats.size());
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "onItemSelected: Naplók betöltése", e);
            }
        }
    }

    private int getNapiOsszSuly(List<NaploActivity.GyakorlatWithSorozat> withSorozats) {
        int result = 0;
        for (int i = 0; i < withSorozats.size(); i++) {
            result += withSorozats.get(i).getMegmozgatottSuly();
        }
        return result;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
