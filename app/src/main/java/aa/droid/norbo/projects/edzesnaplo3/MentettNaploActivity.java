package aa.droid.norbo.projects.edzesnaplo3;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
    private NaploViewModel naploViewModel;
    private ConstraintLayout rootView;

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

        rootView = findViewById(R.id.root_mentett_naplo_view);

        naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        naploViewModel.getNaploListLiveData().observe(this, new Observer<List<Naplo>>() {
            @Override
            public void onChanged(List<Naplo> naplos) {
                if(naplos.size() != 0){
                    ArrayAdapter adapter = new ArrayAdapter<>(MentettNaploActivity.this, android.R.layout.simple_spinner_item, naplos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(MentettNaploActivity.this);
                } else {
                    changeEmptyTextView();
                }
            }
        });
    }

    private void changeEmptyTextView() {
        rcnaploview.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        osszsnapisuly.setText(R.string.mentett_naplo_empty);

        ConstraintSet cntset = new ConstraintSet();
        cntset.clone(rootView);
        cntset.connect(R.id.tvMOsszsuly, ConstraintSet.TOP, R.id.root_mentett_naplo_view, ConstraintSet.TOP,0);
        cntset.applyTo(rootView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mentett_naplok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_naplok_alldelete) {
            naploViewModel.deleteAll();
            SorozatViewModel sorozatViewModel = new ViewModelProvider(this)
                    .get(SorozatViewModel.class);
            sorozatViewModel.deleteAll();
            changeEmptyTextView();
        }
        return super.onOptionsItemSelected(item);
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
